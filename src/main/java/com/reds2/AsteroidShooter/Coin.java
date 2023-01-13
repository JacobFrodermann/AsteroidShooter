package com.reds2.AsteroidShooter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.reds2.AsteroidShooter.util.Util;

public class Coin {
    double x, y, anim;
    boolean h = true;
    public static final BufferedImage[] IMGS = Util.loadAnim("coin_",11);
    Rectangle getCol(){return new Rectangle((int)x,(int)y,32,32);}
    
    Coin(int x, int y) {
        this.x = x;
        this.y = y;
        anim = 0;
    }

    public static void drawAll(Graphics2D g, Iterable<Coin> i){
        for (Coin c : i) {
            g.drawImage(IMGS[(int) c.anim],(int) c.x,(int) c.y,32,32,null);
            c.y += 2.5;
            c.anim += .1;
            c.anim %= 11;
        }
    }
}
