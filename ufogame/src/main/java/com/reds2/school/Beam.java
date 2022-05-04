package com.reds2.school;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Beam {
    Rectangle r;
    AffineTransform t;
    double rot;
    Beam(double x, double y,double r) {
        rot = r;
        this.r = new Rectangle((int)x+40,(int)y+60,10,35);
        t = new AffineTransform();
        t.rotate(r+Game.HALF_PI,(double)this.r.x+5,(double)this.r.y+17.5);
    }
}
