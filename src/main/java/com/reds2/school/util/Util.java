package com.reds2.school.util;

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
}
