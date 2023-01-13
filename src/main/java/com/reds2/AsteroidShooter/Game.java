package com.reds2.AsteroidShooter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.reds2.AsteroidShooter.util.Util;

import lombok.Getter;



public class Game implements State{
	@Getter int ScreenX = 540, ScreenY = 1080;
	BufferedImage[] ship;
	private BufferedImage bg, menu;
	private int anim=0;
	private double SceneY=0;
	double x=250,y=800,time = 0;
	private ArrayList<Integer> keys = new ArrayList<Integer>();
	private	double xV = 0,yV=0,rot=-Math.PI/2, timer;
	private List<Beam> beams = new ArrayList<Beam>();
	Beam[][] template = new Beam[5][];
	Boolean debug = false, death = false,renderParticles=false;
	private int delay = 0;
	int[] xP = new int[10] ,yP = new int[10];
	Font f = new Font("h",Font.BOLD,150), f2 = new Font("g",Font.PLAIN,20);
	private List<Asteriod> asteroids = new ArrayList<Asteriod>();
	Rectangle ShipCol  = new Rectangle((int) x+29,(int) y+38, 24, 40);
	private List<Particle> particles = new ArrayList<Particle>();
	private Rectangle[] Buttons = new Rectangle[2];
	private BufferedImage[][] astAtlas = new BufferedImage[8][8];
	private int tier = 2,Highscore;
	int lives = Main.INSTANCE.settings.lives;
	int reduction = 0;
    int inv=0;
	long frameTime = 0;
	Boolean paused = false;
	int ptime = 0;
	List<Coin> coins = new ArrayList<Coin>();


	Game(){
		try{Highscore = Main.INSTANCE.loadScore();}catch(Exception e){e.printStackTrace();}
		Buttons[0] = new Rectangle(270,455,60,60);
		Buttons[1] = new Rectangle(150,535,220,60);
		ship = new BufferedImage[5];
		for(int i=0;i<ship.length;i++){
			ship[i]=Util.load("Ship"+i+"_"+ Main.INSTANCE.skin);
		}
		bg  = Util.load("GameBG");
		
		BufferedImage atlas = Util.load("asteriodAtlas");
		for (int i=0; i<astAtlas.length; i++) {
			for (int j = 0; j<astAtlas[i].length; j++) {
				astAtlas[i][j] = atlas.getSubimage(i*102+20, j*102+30, 107, 112);
			} 
		}
		template[0] = new Beam[1];
		template[0][0] = new Beam(0,0,0,0,0);
 
		template[1] = new Beam[2];
		template[1][0] = new Beam(0,0,0,0,0);
		template[1][1] = new Beam(0,0,0,0,0);

		template[2] = new Beam[3];
		template[2][0] = new Beam(0,0,0,0,0);
		template[2][1] = new Beam(0,0,.26,0,0);
		template[2][2] = new Beam(0,0,-.26,0,0);

		template[3] = new Beam[5];
		template[3][0] = new Beam(0,0,0,0,0);
		template[3][1] = new Beam(3,5,.261799,0,0);
		template[3][2] = new Beam(-3,5,-.261799,0,0);
		template[3][3] = new Beam(0,0,.1309,0,0);
		template[3][4] = new Beam(0,0,-.1309,0,0);

		template[4]=new Beam[6];
		template[4][0] = new Beam(0,0,0,0,0);
		template[4][1] = new Beam(0,0,.261799,0,0);
		template[4][2] = new Beam(0,0,-.261799,0,0);
		template[4][3] = new Beam(-.1,0,0,0,0);
		template[4][4] = new Beam(0,0,.265,0,0);
		template[4][5] = new Beam(0,0,-.265,0,0);
	}

	void init() {
		lives = Main.INSTANCE.settings.lives;
		renderParticles = Main.INSTANCE.settings.particles;
	}

	@Override
	public BufferedImage draw() {
		inv --;
		frameTime = System.currentTimeMillis();
		ShipCol.x=(int) x+29;
		ShipCol.y=(int) y+33;
		BufferedImage result = new BufferedImage(ScreenX, ScreenY, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	  
		g.drawImage(bg,0, ((int)SceneY%2160)-2160, null);
		g.drawImage(bg,0, (int)SceneY%2160, null);
		SceneY+=0.1;
		
		for (int i =0;i<lives+1;i++){
			g.drawImage(ship[(anim+i)%4],170+60*i,0, 40,60, null);
		}

		Particle.draw(g,particles);	
		Asteriod.drawAll(asteroids,g,astAtlas);
		Coin.drawAll(g,coins);

		try{coins = coins.stream().filter(c->!(c.y<-35)).collect(Collectors.toList());}catch (Exception e){}


		g.setColor(Color.red);
		try{beams = beams.stream().filter(i->!(i.r.x<-35 || i.r.x>575 || i.r.y<-35 || i.r.y>1105)).collect(Collectors.toList());}catch (Exception e){}
		try{beams.stream().forEach((i)->{
			AffineTransform trans = new AffineTransform();
			trans.rotate(i.rot,i.r.getCenterX(),i.r.getCenterY());
			g.setTransform(trans);
			g.fill(i.r);
			if(!paused){i.r.x += i.xV;
			i.r.y += i.yV;}
		});}catch(Exception e){}
		g.setTransform(new AffineTransform());

		if(!(inv>0 && inv%10<5)){                      // Draw Ship
			AffineTransform t = g.getTransform();
			t.rotate(rot+Math.PI/2,x+40,y+60);
			g.setTransform(t);
			g.drawImage(ship[anim],(int)x,(int)y,80,120,null);
			g.setTransform(new AffineTransform());
		}

		if (!paused) tick(g);
		
		g.setFont(f2);                                  // Draw Highscore
		g.setColor(Color.white);
		g.drawString(String.valueOf((int)time), 460, 20);
		g.drawString("Highscore: " + Highscore,15,20);

		keyboradcheck();
		
		if (paused) {
			g.drawImage(menu, 110, 400, null);}

		ptime --;

		frameTime = System.currentTimeMillis()-frameTime;
		return result;
	}



	private void shoot(double rotation) {
		if(!death){
			for (Beam b : template[tier]){
				beams.add(new Beam(b,x,y,rotation,xV,yV));
			}
		}
	}

	@Override
	public void press(KeyEvent e) {
		if (!keys.contains(e.getKeyCode())){
			keys.add(e.getKeyCode());
		}   
	}

	@Override
	public void release(KeyEvent e) {
		keys.remove((Object)e.getKeyCode());
	}

	@Override
	public void click(MouseEvent e, Dimension d) {
		int x = (e.getX()-(d.width-d.height/2)/2)*1080/d.height;
		int y = e.getY()*1080/d.height;
		Point p = new Point(x,y);

		if(death) {
			int which=0;
			for (Rectangle i:Buttons){
				if (i.contains(p)){
					break;
				}
				which++;
			}
			if (which == 0) Main.INSTANCE.current = Main.INSTANCE.menu;
			else reset();
		} else if (paused){
			if (Buttons[0].contains(p)) {
				Main.INSTANCE.current = Main.INSTANCE.menu;
			} else if (Buttons[1].contains(p)) reset();
			else paused = false;
		} 
		else {
			double rotation = Math.atan((this.y-y)/(this.x-x));  
			if(x<this.x){rotation+=Math.PI;}
			if (delay<0){
				delay = Main.INSTANCE.settings.cooldown+5;
				shoot(rotation);
			}	
		}
	}

	@Override
	public void drag(MouseEvent e, Dimension d) {
		click(e,d);
	}

	@Override
	public void m_release(MouseEvent e, Dimension d) {}
	
	void bulkCol(){
		asteroids.forEach(x -> {
		x.x += x.xV*2;
		x.y += x.yV*2;
		try {
			beams.forEach((Beam i)->{
			if (x.col.intersects(i.r)){if(renderParticles){particles.add(new Particle(i.r.x-5,i.r.y,15,new Color(150,40,40)));} beams.remove(i);}//true -> concurrent modification excetion
		});
		} catch (Exception e) {
			x.hp --;
		}
		});
	}
	void reset(){
		paused = false;
		time = 0;
		asteroids.clear();
		beams.clear();
		x=250;
		y=800;
		xV=0;
		yV=0;
		rot = -Math.PI/2;
		death = false;
		anim=0;
		tier=0;
		lives = Main.INSTANCE.settings.lives;
		reduction = 0;
	}
	void death() {
		if(renderParticles)particles.addAll(Particle.Explosion(x+20, y+40,xV,yV, new Color(240, 140, 33), 120));
		if (!death){
			if (lives == 0) {
				death = true;
				menu = new GameMenu(time,Highscore,true);
				if(Highscore<time){
					Highscore = (int) Math.floor(time);
					Main.INSTANCE.saveHighscore((int) Math.floor(time));
				}	
			} else {
				lives --;
				inv = Main.INSTANCE.settings.inv*10;
				reduction += Main.INSTANCE.settings.red*10;
				tier--;
				if (tier == -1){tier = 0;}
				if ((time - reduction)<0){reduction=(int)time;}
				x=250;
				y=800;
				rot = -Math.PI/2;
			} 
			
		}
	}
	void move(){
		x += xV;
		y += yV;
		xV /= 1.1;
		yV /= 1.1;
	}
	void boundaryCheck(Graphics2D g){
		if(x<-75 || x>610 || y < -100 || y>1020){//Timer
			g.setColor(Color.red);
			g.drawLine((int)ShipCol.getCenterX(),(int)ShipCol.getCenterY(), 270, 540);
			g.setFont(f);
			g.drawString(String.valueOf((int)Math.floor(timer)), 200, 400);
			timer -= 1d/60d;
			if (timer<=0){
				death();
				timer = 0.9;
			}
		} else {timer = 3d;}
	}
	void keyboradcheck(){
		if (!paused && !death) {
			if (keys.contains(38)){
				xV += (double)Main.INSTANCE.settings.v/5.5*Math.cos(rot);
				yV += (double)Main.INSTANCE.settings.v/5.5*Math.sin(rot);
				if(renderParticles)particles.add(new Particle(
					(int) ShipCol.getCenterX()-10+new Random().nextInt(10),
					(int) ShipCol.getCenterY(),(int) -(Math.cos(rot)*Main.INSTANCE.settings.v*2),(int) -(Math.sin(rot)*Main.INSTANCE.settings.v*2),
					5,
					new Color(235,197,21,75)));
			}
			if (keys.contains(40)){
				xV /= 1.5;
				yV /= 1.5;
			}
			if (keys.contains(37)){
				rot -= 0.1;
			}
			if (keys.contains(39)){
				rot += 0.1;
			}
			if (keys.contains(32)){
				if (delay<0){
					delay = Main.INSTANCE.settings.cooldown+5;
					shoot(rot);
				}
			}
		}
		if (keys.contains(KeyEvent.VK_ESCAPE) && ptime < 0) {paused =! paused;menu = new GameMenu(time,Highscore,false);ptime = 10;}
	}
	void animate(){
		if (new Random().nextInt(25)==1){anim++;anim =anim%4;}
	}
	void spawnAsteroid(){
		if (new Random().nextInt((10-Main.INSTANCE.settings.astoids)*10)==1){
			asteroids.add(new Asteriod());
		}
	}
	void tick(Graphics2D g) {
		if(!death){
			animate();
			keyboradcheck();
			spawnAsteroid();
			time += 1d/60d;	
		} else{
			anim = 4;
			if (keys.contains(10)){
				reset();
			}
			g.drawImage(menu, 110, 400, null);
		}

		move();

		delay--;

		boundaryCheck(g);

		coins.forEach((Coin c) -> {
			if (c.getCol().intersects(ShipCol)) {
				//TODO Add Sparkling
			}
		});		

		asteroids.forEach((i)->{
			if(i.hp<0){
				if(renderParticles){
					if(i.type > 2){
						particles.addAll(Particle.Explosion(i.x,i.y,i.xV,i.yV,new Color(200,200,200),i.s));	
					} else {
						particles.addAll(Particle.Explosion(i.x,i.y,i.xV,i.yV,new Color(235,215,11),i.s));   
					}
				}
				if(tier !=4 && i.type < 3){
					tier++;
					coins.add(new Coin((int) i.x,(int) i.y));
				}
			}
		});
		try{asteroids=asteroids.stream().filter(i->!(i.hp<0||i.y>1920)).collect(Collectors.toList());}catch(Exception e){}

		try{coins=coins.stream().filter(i->i.getCol().intersects(ShipCol)).collect(Collectors.toList());}catch(Exception e){}

		bulkCol();
	}
}