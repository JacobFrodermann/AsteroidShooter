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

class Asteriod{
    private Random rng = new Random();
    double x,y,xV,yV,rot,rV;
    int s=new Random().nextInt(30)+30+(int) Main.INSTANCE.game.time, hp=s/20;
    private Dimension aim;
    Ellipse2D col;
    public int type = 0;
    Asteriod(){
        type = rng.nextInt(7);
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
        List<Asteriod> out = asteroids;
        try {
            out.forEach((i)->{
                if(i.y>1090||i.hp==0){
                    particles.addAll(out.size(), Particle.Explosion((int)i.x,(int) i.y, new Color(50,50,50), i.s)); 
                    out.remove(i);
                }});
                return out;
            } catch (Exception e){return out;}    
    };
    public static void bulkDraw(List<Asteriod> asteroids, Graphics2D g,BufferedImage[] img) {
        asteroids.forEach((i)->{
        AffineTransform tr = new AffineTransform();
        tr.rotate(i.rot,i.x+i.s/2,i.y+i.s/2);
        i.rot +=i.rV;
        g.setTransform(tr);
        g.drawImage(img[i.type], (int)i.x, (int)i.y,i.s,i.s, null);
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