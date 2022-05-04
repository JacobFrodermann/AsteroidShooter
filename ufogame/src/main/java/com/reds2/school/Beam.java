package com.reds2.school;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Beam {
    Rectangle r;
    AffineTransform t;
    Beam(double x, double y,double r) {
        this.r = new Rectangle((int)x,(int)y,5,15);
        t = new AffineTransform();
        t.rotate(r,(double)this.r.x+2.5,(double)this.r.y+7.5);
    }
}