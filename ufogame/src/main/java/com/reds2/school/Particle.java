package com.reds2.school;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Graphics2D;

public class Particle {
	int stage;
	Color color;
	int xV = 0,yV = 0,x,y;
	Particle(int x, int y,int stage,Color color){
		this.stage = stage*2;
		this.color = color;
		this.x = x;
		this.y = y;
	}
	Particle(int x, int y,int xV, int yV,int stage,Color color){
		this.stage = stage*2;
		this.color = color;
		this.xV = xV;
		this.yV = yV;
		this.x = x;
		this.y = y;
	}
	static List<Particle> Explosion(double x,double y,Color c,int size){
		Random rng = new Random();
		List<Particle>out = new ArrayList<Particle>();
		for (int i = rng.nextInt(5);i<size/10;i++){
			out.add(new Particle((int)x,(int) y,rng.nextInt(4)-2,rng.nextInt(4)-2, size/4, c));
		}
		return out;
	}
	static void draw(Graphics2D g,Particle p){
		Random rng = new Random();
		int r = p.color.getRed(),g_ = p.color.getGreen(), b = p.color.getBlue();
		try{
			for (int i = 0;i<p.stage/2;i++){
			g.setColor(new Color(r+rng.nextInt(20)-10,g_+rng.nextInt(20)-10,b+rng.nextInt(20)-10,rng.nextInt(200)+50));
			g.fillRect(p.x+rng.nextInt(10)-5, p.y+rng.nextInt(10)-5, rng.nextInt(5)+5, rng.nextInt(5)+5);
			}
		} catch(IllegalArgumentException e){System.out.println(r+"/"+g_+"/"+b);}
		p.stage--;
		p.x+=p.xV;
		p.y+=p.yV;
	}
	static public void draw(Graphics2D g, List<Particle> p) {
		p.forEach((i) -> {
			Particle.draw(g,i);
		});
	}
}
