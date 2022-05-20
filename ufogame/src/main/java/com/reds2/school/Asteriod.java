package com.reds2.school;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import com.reds2.school.util.*;

class Asteriod{
    private Random rng = new Random();
    double x,y,xV,yV,rot,rV;
    int s=new Random().nextInt(30)+30+(int) Main.INSTANCE.game.time, hp=s/20;
    private Dimension aim;
    Ellipse2D col;
    static private BufferedImage img = Util.load("asteriod1");
    Asteriod(){
        x = rng.nextInt(740)-200;
        y = -50;
        aim = new Dimension(rng.nextInt(210)+150,rng.nextInt(60)+500);
        rot = Math.atan((y-aim.height)/(x-aim.width));  
		if(aim.width<x){rot+=Math.PI;}
        xV = (rng.nextDouble()*1.5+Math.sqrt(Main.INSTANCE.game.time)+1)*Math.cos(rot);
        yV = (rng.nextDouble()*1.5+Math.sqrt(Main.INSTANCE.game.time)+1)*Math.sin(rot); 
        rV = rng.nextDouble()*0.5/(double)Math.sqrt(s);
        col = new Ellipse2D.Double(x,y,(double)s,(double)s);
    }
    public static List<Asteriod> clean(List<Asteriod> asteroids,List<Particle> particles) {
        try {
            asteroids.forEach((i)->{
                if(i.y>1090||i.hp==0){
                    particles.addAll(asteroids.size(), Particle.Explosion((int)i.x,(int) i.y, new Color(50,50,50), i.s)); 
                    asteroids.remove(i)
                }
            }
        }catch (Exception e){}    
    };
    public static void bulkDraw(List<Asteriod> asteroids, Graphics2D g) {
        asteroids.forEach((i)->{
        AffineTransform tr = new AffineTransform();
        tr.rotate(i.rot,i.x+i.s/2,i.y+i.s/2);
        i.rot +=i.rV;
        g.setTransform(tr);
        g.drawImage(img, (int)i.x, (int)i.y,i.s,i.s, null);
        i.x+=i.xV;
        i.y+=i.yV;
        i.col.setFrame(i.x, i.y,(int) i.s,(int) i.s);
        //g.setTransform(new AffineTransform());
        //g.draw(i.col);
        if (i.col.intersects(Main.INSTANCE.game.colR)){Main.INSTANCE.game.death();};
    });
    g.setTransform(new AffineTransform());
    }
}