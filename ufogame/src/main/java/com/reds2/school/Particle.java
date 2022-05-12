package com.reds2.school;

import java.awt.Color;

public class Particle {
    double stage;
    Color color;
    double xV = 0,yV = 0,x,y;
    Particle(double x, double y,double stage,Color color){
        this.stage = stage;
        this.color = color;
        this.x = x;
        this.y = y;
    }
    Particle(double x, double y,double xV, double yV,double stage,Color color){
        this.stage = stage;
        this.color = color;
        this.xV = xV;
        this.yV = yV;
        this.x = x;
        this.y = y;
    }
}
