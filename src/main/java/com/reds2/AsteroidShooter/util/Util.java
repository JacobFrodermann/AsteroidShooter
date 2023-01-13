package com.reds2.AsteroidShooter.util;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Util {
    public static BufferedImage load(String name){
        name = name+".png";
        try{
            return ImageIO.read(Util.class.getClassLoader().getResourceAsStream(name));   
        } catch (Exception e){
            
            e.printStackTrace();
            System.out.println("while loading: "+name);
        }
        return null;
    }

    public static BufferedImage[] loadAnim(String name, int i) {
        BufferedImage[] arr = new BufferedImage[i];
        for (int l = 0; l < i; l++) {
            arr[l] = load(name + l);
        }
        return arr;
    }
}
