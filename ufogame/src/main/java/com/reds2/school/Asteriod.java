package com.reds2.school;

import java.util.Random;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;

class Asteriod{
    AffineTransform t;
    private Random rng = new Random();
    double x,y,xV,yV,rot;
    int s=new Random().nextInt(20)+35;
    private Dimension aim;
    Asteriod(){
        x = rng.nextDouble(740)-200;
        y = -50;
        aim = new Dimension(rng.nextInt(210)+150,rng.nextInt(60)+500);
        System.out.println(aim.width+"/"+aim.height);
        rot = Math.atan((y-aim.height)/(x-aim.width));  
		if(aim.width<x){rot+=Math.PI;}
        xV = rng.nextInt(2)+1.5d*Math.cos(rot);
        yV = rng.nextInt(2)+1.5d*Math.sin(rot);
        rot = new Random().nextDouble(0.15);
        t = new AffineTransform();
    }
}