package com.reds2.school;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particle {
	int stage;
	Color color;
	int xV = 0,yV = 0,x,y;
	Particle(int x, int y,int stage,Color color){
		this.stage = stage;
		this.color = color;
		this.x = x;
		this.y = y;
	}
	Particle(int x, int y,int xV, int yV,int stage,Color color){
		this.stage = stage;
		this.color = color;
		this.xV = xV;
		this.yV = yV;
		this.x = x;
		this.y = y;
	}
	static List<Particle> Explosion(int x,int y,Color c,int size){
		Random rng = new Random();
		List<Particle>out = new ArrayList<Particle>();
		for (int i = rng.nextInt(5);i>size;i++){
			out.add(new Particle(x, y,rng.nextInt(4)-2,rng.nextInt(4)-2, size/4, c));
		}
		return out;
	}
}
