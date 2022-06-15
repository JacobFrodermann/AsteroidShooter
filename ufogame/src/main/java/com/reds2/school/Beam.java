package com.reds2.school;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Beam {
    Rectangle r;
    AffineTransform t;
    Color c;
    Boolean moving = false;
    double xV,yV;
    Beam(double x, double y,double r,Color c) {
        this.c = c;
        this.r = new Rectangle((int)x,(int)y,5,15);
        xV = 5 * Math.cos(r);
        yV = 5 * Math.sin(r);
        t = new AffineTransform();
        t.rotate(r,(double)this.r.x+2.5,(double)this.r.y+7.5);
    }
}
