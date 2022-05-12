package com.reds2.school;

import java.awt.Color;

public class Particle {
    int stage;
    Color color;
    int xV = 0,yV = 0,x,y;
    Particle(int x, int y,int stage,Color color){
        this.stage = stage;
        this.color = color;
        this.x = x;
        this.y = y;
    }
    Particle(int x, int y,int xV, int yV,int stage,Color color){
        this.stage = stage;
        this.color = color;
        this.xV = xV;
        this.yV = yV;
        this.x = x;
        this.y = y;
    }
}
