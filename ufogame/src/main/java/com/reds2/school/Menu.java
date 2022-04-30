package com.reds2.school;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
//import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Menu implements State{
    String state = "Main";
    BufferedImage bg, start, settings;
    Rectangle startR = new Rectangle(140,363,256,87), settingsR = new Rectangle(122,470,291,70);
	static final Logger log = LoggerFactory.getLogger(Main.class);

    Menu(Dimension size){
        int i = 0;
        try {
            bg = ImageIO.read(Menu.class.getClassLoader().getResourceAsStream("actionfieldBg1.png"));i++;
            start = ImageIO.read(Menu.class.getClassLoader().getResourceAsStream("Start.png"));i++;
            settings = ImageIO.read(Menu.class.getClassLoader().getResourceAsStream("Settings.png"));i++;
        } catch (IOException e) {
            log.error("Couldn't load Bg img" + i);
        }
    }

    public BufferedImage draw(){
        BufferedImage result = new BufferedImage(540, 1080, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g.drawImage(bg, 0, 0, 540, 1080, null);
        g.drawImage(start, 140,363,256,87,null);
        g.drawImage(settings,122,470,291,70,null);
        
        
        //g.setColor(Color.RED);
        //g.fillRect(0, 0, size.width, size.height);
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
        int x = e.getX()+d.width/540-d.width/3;
        int y = e.getY()+d.height/1080;
        System.out.println(y);
        if (startR.contains(new Point(x,y))) {
            Main.INSTANCE.current = new Game();
        }
        if(settingsR.contains(new Point(x,y))){
            Main.INSTANCE.current = new Settings();
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
