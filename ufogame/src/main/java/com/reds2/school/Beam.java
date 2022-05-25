package com.reds2.school;

import java.awt.Rectangle;
public class Beam {
    Rectangle r;
    double xV,yV,rot;
    private double s;

    Beam(double x, double y,double rot,double shXV,double shYV) {
        s = Math.sqrt(Math.pow(shXV,2)+Math.pow(shYV,2));
        xV = (Main.INSTANCE.settings.beamV+s) * Math.cos(rot);
        yV = (Main.INSTANCE.settings.beamV+s) * Math.sin(rot);
        this.r = new Rectangle((int)x+35,(int)y+55,35 ,10);
        this.rot = rot;
    }
    Beam(Beam b,double x, double y,double rotation,double shXV,double shYV){
        s = Math.sqrt(Math.pow(shXV,2)+Math.pow(shYV,2));
        rot = rotation+b.rot;
        xV = (Main.INSTANCE.settings.beamV+s) * Math.cos(rot);//+b.xV;
        yV = (Main.INSTANCE.settings.beamV+s) * Math.sin(rot);//+b.yV;
        r = new Rectangle((int)x+b.r.x,(int)y+b.r.y,35 ,10);
    }
}
