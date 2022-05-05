package com.reds2.school;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.awt.Stroke;

import javax.imageio.ImageIO;
import javax.swing.border.StrokeBorder;

public class Game implements State{
	private BufferedImage[] coin,asteriod,ship;
	private BufferedImage bg;
	private int anim=0;
	private double SceneY=0;
	double x=250,y=800;
	private ArrayList<Integer> keys = new ArrayList<Integer>();
	private	double xV = 0,yV=0,rot=-Math.PI/2, Timer = 10;
	static final double HALF_PI=Math.PI/2,QUARTER_PI=Math.PI/4;
	private List<Beam> beams = new ArrayList<Beam>();
	Boolean debug = false, death = false;
	private int delay = 0;
	Polygon col;
	int[] xP = new int[10] ,yP = new int[10];
	Font f = new Font("h",Font.BOLD,150);
	Game(){
		switch(Main.INSTANCE.skin){
			case 0:
				col = new Polygon();
				col.addPoint(41, 22);
				col.addPoint(29, 38);
				col.addPoint(29, 64);
				col.addPoint(13, 64);
				col.addPoint(13,76);
				col.addPoint(68, 76);
				col.addPoint(69,62);
				col.addPoint(53,62);
				col.addPoint(53,37);
				break;
		}
		System.arraycopy(col.xpoints,0,xP ,0, 10);
		System.arraycopy(col.ypoints,0,yP ,0, 10);
		ship = new BufferedImage[4];
		try {
			for(int i=0;i<4;i++){
				System.out.println("Ship"+i+"_"+ Main.INSTANCE.skin+".png");
				ship[i]=ImageIO.read(Game.class.getClassLoader().getResourceAsStream("Ship"+i+"_"+ Main.INSTANCE.skin+".png"));
				System.out.println(i);
			}
			bg  = ImageIO.read(Game.class.getClassLoader().getResourceAsStream("GameBG.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public BufferedImage draw() {
		for (int i = 0;i<10;i++){
			col.xpoints[i]=xP[i]+(int)x;
			col.ypoints[i]=yP[i]+(int)y;
		}
		BufferedImage result = new BufferedImage(540, 1080, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	  
		g.drawImage(bg,0, ((int)SceneY%2160)-2160, null);
		g.drawImage(bg,0, (int)SceneY%2160, null);
		SceneY+=0.1;

		g.setColor(Color.red);
		beams = beams.stream().filter(i->!(i.r.x<-35 || i.r.x>575 || i.r.y<-35 || i.r.y>1105)).collect(Collectors.toList());
		beams.stream().forEach(i->{
			AffineTransform trans = new AffineTransform();
			trans.rotate(i.rot,i.r.getCenterX(),i.r.getCenterY());
			g.setTransform(trans);
			g.fill(i.r);
			i.r.x += i.xV;
			i.r.y += i.yV;
		});
		g.setTransform(new AffineTransform());

		AffineTransform t = g.getTransform();
		t.rotate(rot+HALF_PI,x+40,y+60);

		Point2D[] in = new Point2D[10],out = new Point2D[10];
		for (int i = 0;i<10;i++){
			in[i]=new Point2D.Double(col.xpoints[i],col.ypoints[i]);
		}
		t.transform(in, 0, out, 0, 10);
		for (int i = 0;i<10;i++){
			col.xpoints[i]=(int)out[i].getX();
			col.ypoints[i]=(int)out[i].getY();
		}
		g.setTransform(t);
		g.drawImage(ship[anim],(int)x,(int)y,80,120,null);
		g.setTransform(new AffineTransform());

		if (new Random().nextInt(50)==1){anim++;anim =anim%4;}
		if (keys.contains(38)){
			xV += 1*Math.cos(rot);
			yV += 1*Math.sin(rot);
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
		x += xV;
		y += yV;
		xV /= 1.1;
		yV /= 1.1;

		if (keys.contains(32)){
			if (delay<0){
				delay = 10;
				shoot();
			}
			delay--;
		}
		if(x<-75 || x>610 || y < -100 || y>1020){
			g.setColor(Color.red);
			g.drawLine(col.xpoints[0], col.ypoints[0], 270, 540);
			g.setFont(f);
			g.drawString(String.valueOf((int)Math.floor(Timer)), 200, 400);
			Timer -= 1d/60d;
			if (Timer<=0){
				death = true;
				System.out.println("rip");
			}
		} else {Timer = 10.9;}
		
		//g.fill(col);
		
		return result;
	}

	private void shoot() {
		beams.add(new Beam(x,y,rot,xV,yV));
		if(debug){System.out.println("Debug: Beam rotation, resulting x y Vel"+rot+" "+beams.get(beams.size()-1).xV+" "+beams.get(beams.size()-1).yV);}
		System.out.println(col.xpoints[0]);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drag(MouseEvent e, Dimension d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void m_release(MouseEvent e, Dimension d) {
		// TODO Auto-generated method stub
		
	}
	
}
