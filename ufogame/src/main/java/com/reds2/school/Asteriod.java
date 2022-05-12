package com.reds2.school;

import java.util.Random;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;

class Asteriod{
    private Random rng = new Random();
    double x,y,xV,yV,rot,rV;
    int s=new Random().nextInt(30)+30+(int) Main.INSTANCE.game.time, hp=s/20;
    private Dimension aim;
    Ellipse2D col;
    Asteriod(){
        x = rng.nextDouble(740)-200;
        y = -50;
        aim = new Dimension(rng.nextInt(210)+150,rng.nextInt(60)+500);
        rot = Math.atan((y-aim.height)/(x-aim.width));  
		if(aim.width<x){rot+=Math.PI;}
        xV = rng.nextInt(2)+Main.INSTANCE.game.time+1*Math.cos(rot);
        yV = rng.nextInt(2)+Main.INSTANCE.game.time+1*Math.sin(rot); 
        rV = rng.nextDouble(.2)/(double)s;
        col = new Ellipse2D.Double(x,y,(double)s,(double)s);
    }
}