package com.reds2.school;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reds2.school.util.Util;


public class Game implements State{
	private static final Logger log = LoggerFactory.getLogger(Game.class);
	BufferedImage[] ship;
	private BufferedImage bg, menu;
	private int anim=0;
	private double SceneY=0;
	double x=250,y=800,time = 0;
	private ArrayList<Integer> keys = new ArrayList<Integer>();
	private	double xV = 0,yV=0,rot=-Math.PI/2, timer = 10;
	static final double HALF_PI=Math.PI/2,QUARTER_PI=Math.PI/4;
	private List<Beam> beams = new ArrayList<Beam>();
	Beam[][] template = new Beam[3][];
	Boolean debug = false, death = false;
	private int delay = 0;
	int[] xP = new int[10] ,yP = new int[10];
	Font f = new Font("h",Font.BOLD,150), f2 = new Font("g",Font.PLAIN,20);
	private List<Asteriod> asteroids = new ArrayList<Asteriod>();
	Rectangle colR  = new Rectangle((int) x+29,(int) y+38, 24, 40);
	private List<Particle> particles = new ArrayList<Particle>();
	private Rectangle[] Buttons = new Rectangle[2];
	private BufferedImage[][] astAtlas = new BufferedImage[8][8];
	private int tier = 0,Highscore;
	long frameTime = 0;
	Game(){
		try{Highscore = Main.INSTANCE.loadScore();}catch(Exception e){e.printStackTrace();}
		Buttons[0] = new Rectangle(270,455,60,60);
		Buttons[1] = new Rectangle(150,535,220,60);
		ship = new BufferedImage[5];
		for(int i=0;i<ship.length;i++){
			ship[i]=Util.load("Ship"+i+"_"+ Main.INSTANCE.skin);
			//System.out.println(i);
		}
		bg  = Util.load("GameBG");
		
		BufferedImage atlas = Util.load("asteriodAtlas");
		for (int i=0; i<astAtlas.length; i++) {
			for (int j = 0; j<astAtlas[i].length; j++) {
				astAtlas[i][j] = atlas.getSubimage(i*102+20, j*105+20, 107, 112);
			}
		}
		/*int n = 0;
		for (BufferedImage[] i : astAtlas){
			for (BufferedImage j : i){
				try {
					ImageIO.write(j, "png",new File("testing/"+n+".png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				n++;
			}
		}*/

		template[0] = new Beam[1];
		template[0][0] = new Beam(0,0,0,0,0);

		template[1] = new Beam[3];
		template[1][0] = new Beam(0,0,0,0,0);
		template[1][1] = new Beam(0,0,.261799,0,0);
		template[1][2] = new Beam(0,0,-.261799,0,0);

		template[2] = new Beam[5];
		template[2][0] = new Beam(0,0,0,0,0);
		template[2][1] = new Beam(3,5,.261799,0,0);
		template[2][2] = new Beam(-3,5,-.261799,0,0);
		template[2][3] = new Beam(0,0,.1309,0,0);
		template[2][4] = new Beam(0,0,-.1309,0,0);
	}

	@Override
	public BufferedImage draw() {
		frameTime = System.currentTimeMillis();
		colR.x=(int) x+29;
		colR.y=(int) y+33;
		BufferedImage result = new BufferedImage(540, 1080, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	  
		g.drawImage(bg,0, ((int)SceneY%2160)-2160, null);
		g.drawImage(bg,0, (int)SceneY%2160, null);
		SceneY+=0.1;
		
		Particle.draw(g,particles);	

		g.setColor(Color.red);
		try{beams = beams.stream().filter(i->!(i.r.x<-35 || i.r.x>575 || i.r.y<-35 || i.r.y>1105)).collect(Collectors.toList());}catch (Exception e){}
		try{beams.stream().forEach((i)->{
			AffineTransform trans = new AffineTransform();
			trans.rotate(i.rot,i.r.getCenterX(),i.r.getCenterY());
			g.setTransform(trans);
			g.fill(i.r);
			i.r.x += i.xV;
			i.r.y += i.yV;
		});}catch(Exception e){}
		g.setTransform(new AffineTransform());

		AffineTransform t = g.getTransform();
		t.rotate(rot+HALF_PI,x+40,y+60);

		g.setTransform(t);
		g.drawImage(ship[anim],(int)x,(int)y,80,120,null);
		g.setTransform(new AffineTransform());

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

		asteroids.forEach((i)->{
			if(i.hp<0){
				if(i.type > 2){
					particles.addAll(Particle.Explosion(i.x,i.y,new Color(200,200,200),i.s));	
				} else {
					particles.addAll(Particle.Explosion(i.x,i.y,new Color(235,215,0),i.s));
				    tier++;
					tier = tier % 3;
				}


			}
		});
		try{asteroids=asteroids.stream().filter(i->!(i.hp<0||i.y>1920)).collect(Collectors.toList());}catch(Exception e){}
		Asteriod.bulkDraw(asteroids,g,astAtlas);
		bulkCol();
		
		g.setFont(f2);
		g.setColor(Color.white);
		g.drawString(String.valueOf((int)Math.floor(time)), 460, 20);

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
		if(death){
			int which=0;
			Point p =new Point(x,y);
			for (Rectangle i:Buttons){
				if (i.contains(p)){
					break;
				}
				which++;
			}
			switch (which){
				case 0:
					Main.INSTANCE.current = Main.INSTANCE.menu;
				case 1:
					reset();
					break;
			}
		} else {
			double rotation = Math.atan((this.y-y)/(this.x-x));  
			if(x<this.x){rotation+=Math.PI;}
			if (delay<0){
				delay = 10;
				shoot(rotation);
			}	
		}
	}

	@Override
	public void drag(MouseEvent e, Dimension d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void m_release(MouseEvent e, Dimension d) {

		// TODO Auto-generated method stub
		
	}
	
	void bulkCol(){
		asteroids.forEach(x -> {
		try {
			beams.forEach((Beam i)->{
			if (x.col.intersects(i.r)){particles.add(new Particle(i.r.x-5,i.r.y,15,new Color(150,40,40))); beams.remove(i);}//true -> concurrent modification excetion
		});
		} catch (Exception e) {
			x.hp --;
		}
		});
	}
	void reset(){
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
	}
	void death() throws IOException{
		if (!death){
			log.info("died with score of: "+time+" at " + System.currentTimeMillis());
			death = true;
			menu = new GameMenu(time,Highscore);
			if(Highscore<time){
				Highscore = (int) Math.floor(time);
				Main.INSTANCE.saveHighscore((int) Math.floor(time));
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
			g.drawLine((int)colR.getCenterX(),(int)colR.getCenterY(), 270, 540);
			g.setFont(f);
			g.drawString(String.valueOf((int)Math.floor(timer)), 200, 400);
			timer -= 1d/60d;
			if (timer<=0){
				death = true;
			}
		} else {timer = 10.9;}
	}
	void keyboradcheck(){
		if (keys.contains(38)){
			xV += 1.25d*Math.cos(rot);
			yV += 1.25d*Math.sin(rot);
			particles.add(new Particle((int) colR.getCenterX()-10+new Random().nextInt(10),(int) colR.getCenterY(),(int) (-xV/2.5),(int) (-yV/2.5), 5, new Color(235,197,21,75)));
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
				delay = 10;
				shoot(rot);
			}
		}
	}
	void animate(){
		if (new Random().nextInt(50)==1){anim++;anim =anim%4;}
	}
	void spawnAsteroid(){
		if (new Random().nextInt(40)==1){
			asteroids.add(new Asteriod());
		}
	}
	void Upgrade(){

	}
}
