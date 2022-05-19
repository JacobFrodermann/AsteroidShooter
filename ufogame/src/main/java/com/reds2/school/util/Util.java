package com.reds2.school.util;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.reds2.school.Main;

public class Util {
    public static BufferedImage load(String name){
        name = name+".png";
        try{
            return ImageIO.read(Main.class.getClassLoader().getResourceAsStream(name));   
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
