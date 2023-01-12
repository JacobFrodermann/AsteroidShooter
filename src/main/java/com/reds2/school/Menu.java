package com.reds2.school;

import java.awt.image.BufferedImage;
import com.reds2.school.util.Util;
import lombok.Getter;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Menu implements State{
    @Getter int ScreenX = 540, ScreenY = 1080;
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
        BufferedImage result = new BufferedImage(ScreenX,ScreenY, BufferedImage.TYPE_INT_RGB);
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
    public void press(KeyEvent e) {}
    @Override
    public void release(KeyEvent e) { }

    @Override
    public void click(MouseEvent e, Dimension d) {
        int x = (e.getX()-(d.width-d.height/2)/2)*1080/d.height;
        int y = e.getY()*1080/d.height;
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
    public void drag(MouseEvent e, Dimension d) {}

    @Override
    public void m_release(MouseEvent e, Dimension d) {}
}
