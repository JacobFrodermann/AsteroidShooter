package com.reds2.school;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
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

import javax.imageio.ImageIO;

public class Game implements State{
	private BufferedImage[] asteriod = new BufferedImage[1],ship;
	private BufferedImage bg;
	private int anim=0;
	private double SceneY=0;
	double x=250,y=800,time = 0;
	private ArrayList<Integer> keys = new ArrayList<Integer>();
	private	double xV = 0,yV=0,rot=-Math.PI/2, Timer = 10;
	static final double HALF_PI=Math.PI/2,QUARTER_PI=Math.PI/4;
	private List<Beam> beams = new ArrayList<Beam>();
	Boolean debug = false, death = false;
	private int delay = 0;
	Font f = new Font("h",Font.BOLD,150), f2 = new Font("g",Font.PLAIN,30);
	private List<Asteriod> asteroids = new ArrayList<Asteriod>();
	private Rectangle colR  = new Rectangle((int) x+29,(int) y+38, 24, 40);
	private List<Particle> particles = new ArrayList<Particle>();
	Game(){
		ship = new BufferedImage[5];
		try {
			for(int i=0;i<ship.length;i++){
				System.out.println("Ship"+i+"_"+ Main.INSTANCE.skin+".png");
				ship[i]=ImageIO.read(Game.class.getClassLoader().getResourceAsStream("Ship"+i+"_"+ Main.INSTANCE.skin+".png"));
				//System.out.println(i);
			}
			bg  = ImageIO.read(Game.class.getClassLoader().getResourceAsStream("GameBG.png"));
			asteriod[0]= ImageIO.read(Game.class.getClassLoader().getResourceAsStream("asteriod1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public BufferedImage draw() {
		colR.x=(int) x+29;
		colR.y=(int) y+33;
		BufferedImage result = new BufferedImage(540, 1080, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	  
		g.drawImage(bg,0, ((int)SceneY%2160)-2160, null);
		g.drawImage(bg,0, (int)SceneY%2160, null);
		SceneY+=0.1;

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
		if(!death){//Tastatur steuerung
			if (new Random().nextInt(50)==1){anim++;anim =anim%4;}
			if (keys.contains(38)||keys.contains(KeyEvent.VK_W)){
				xV += 1.1d*Math.cos(rot);
				yV += 1.1d*Math.sin(rot);
			}
			if (keys.contains(40)||keys.contains(KeyEvent.VK_S)){
				xV /= 1.5;
				yV /= 1.5;
			}
			if (keys.contains(37)||keys.contains(KeyEvent.VK_A)){
				rot -= 0.1;
			}
			if (keys.contains(39)||keys.contains(KeyEvent.VK_D)){
				rot += 0.1;
			}
			if (keys.contains(32)){
				if (delay<0){
					delay = 10;
					shoot(rot);
				}
			}
			time += 1d/60d;
		} else{
			anim = 4;
			if (keys.contains(10)){
				reset();
			}
		}

		x += xV;
		y += yV;
		xV /= 1.1;
		yV /= 1.1;


		delay--;
		if(x<-75 || x>610 || y < -100 || y>1020){//Timer
			g.setColor(Color.red);
			g.drawLine((int) colR.getCenterX(), (int) colR.getCenterY(), 270, 540);
			g.setFont(f);
			g.drawString(String.valueOf((int)Math.floor(Timer)), 200, 400);
			Timer -= 1d/60d;
			if (Timer<=0){
				death = true;
			}
		} else {Timer = 5.9;}

		g.setColor(Color.red);

		asteroids.forEach(i->{
			massCol(i);
			if (i.hp == 0){
				Random rng = new Random();
				for (int n = 0;n<i.s/10;n++){
					particles.add(new Particle(i.x+i.s/2,i.y+i.s/2,rng.nextDouble()*3-1.5+i.xV/2.5,rng.nextDouble()*3-1.5+i.yV/2.5,40,new Color(38, 37, 36)));
				}
			}
		});

		particles.forEach(i-> {
			drawParticle(g, i);
		});
		asteroids = asteroids.stream().filter(i->!(i.y>1090||i.hp==0)).collect(Collectors.toList());
		asteroids.forEach((i)->{//asteroiden berrechnungen + render
			AffineTransform tr = new AffineTransform();
			tr.rotate(i.rot,i.x+i.s/2,i.y+i.s/2);
			i.rot +=i.rV;
			g.setTransform(tr);
			g.drawImage(asteriod[0], (int)i.x, (int)i.y,i.s,i.s, null);
			i.x+=i.xV;
			i.y+=i.yV;
			i.col.setFrame(i.x, i.y,(int) i.s,(int) i.s);
			//g.setTransform(new AffineTransform());
			//g.draw(i.col);
			if (i.col.intersects(colR))death = true;
		});
		g.setTransform(new AffineTransform());
		
		if (new Random().nextInt(55-(int)time/2)==1&&!death){
			asteroids.add(new Asteriod());
		}

		g.setFont(f2);
		g.setColor(Color.white);
		g.drawString(String.valueOf((int)Math.floor(time)), 460, 30);
		
		return result;
	}

	private void shoot(double rotation) {
		beams.add(new Beam(x,y,rotation,xV,yV));
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
		double rotation = Math.atan((this.y-y)/(this.x-x));  
		if(x<this.x){rotation+=Math.PI;}
		if (delay<0&&!death){
			delay = 10;
			shoot(rotation);
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
	
	void massCol(Asteriod x){
		try {
			beams.forEach((Beam i)->{
			if (x.col.intersects(i.r)){particles.add(new Particle(i.r.x,i.r.y,30,new Color(199, 106, 56))); beams.remove(i);}//true -> concurrent modification excetion
		});
		} catch (Exception e) {
			x.hp --;
		}
	}
	void drawParticle(Graphics2D g,Particle p){
		Random rng = new Random();
		for (int i = 0;i<p.stage/2;i++){
			g.setColor(new Color(p.color.getRed()+rng.nextInt(20)-10, p.color.getGreen()+rng.nextInt(20)-10, p.color.getBlue()+rng.nextInt(20)-10, rng.nextInt(100)));
			g.fillRect((int) p.x+rng.nextInt(10)-5,(int) p.y+rng.nextInt(10)-5, rng.nextInt(5)+5, rng.nextInt(5)+5);
		}
		p.stage--;
		p.x+=p.xV;
		p.y+=p.yV;
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
}
