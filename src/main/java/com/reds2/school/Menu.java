package com.reds2.school;

import java.awt.image.BufferedImage;
import java.lang.module.ResolutionException;

import com.reds2.school.util.Util;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Menu implements State{
    String state = "Main";
    BufferedImage bg, start, settings, quit;
    Rectangle startR = new Rectangle(140,363,256,87), settingsR = new Rectangle(122,470,291,70), quitR = new Rectangle(160,560,150,87);

    Menu(){
        bg = Util.load("actionfieldBg1");
        start = Util.load("Start");
        settings =Util.load("Settings");
        quit = Util.load("Quit");
    }

    public BufferedImage draw(){
        BufferedImage result = new BufferedImage(540, 1080, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g.drawImage(bg, 0, 0, 540, 1080, null);
        g.drawImage(start, 140,363,256,87,null);
        g.drawImage(settings,122,470,291,70,null);
        g.drawImage(quit, 180, 560, null);
        
        return result;
    }

    @Override
    public void press(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void release(KeyEvent e) { 
        // TODO Auto-generated method stub
        
    }

    @Override
    public void click(MouseEvent e, Dimension d) {
        int x = (e.getX()-(d.width-d.height/2)/2)*1080/d.height;
        int y = e.getY()*1080/d.height;
        //System.out.println(x);
        if (startR.contains(new Point(x,y))) {
            Main.INSTANCE.current = Main.INSTANCE.game;
            Main.INSTANCE.game.init();
        }
        if(settingsR.contains(new Point(x,y))){
            Main.INSTANCE.current = Main.INSTANCE.settings;
        }
        if (quitR.contains(new Point(x,y))){
            System.exit(0);
        }
    }

    @Override
    public void drag(MouseEvent e, Dimension d) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void m_release(MouseEvent e, Dimension d) {
        // TODO Auto-generated method stub
        
    }
}
