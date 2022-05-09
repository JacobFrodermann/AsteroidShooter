package com.reds2.school;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;

class Asteriod{
    BufferedImage img;
    AffineTransform t;
    private Random rng = new Random();
    double x,y,xV,yV,rot;
    private Dimension aim;
    Asteriod(){
        x = rng.nextDouble(940)-200d;
        y = -50;
        aim = new Dimension(rng.nextInt(100)+210,rng.nextInt(500)+60);
        rot = Math.atan((y-aim.height)/(x-aim.width));  
		if(aim.width<x){rot+=Math.PI;}
        xV = rng.nextInt(2)+1*Math.cos(rot);
        yV = rng.nextInt(2)+1*Math.sin(rot);
        t = new AffineTransform();
    }

    
}