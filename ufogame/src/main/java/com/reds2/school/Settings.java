package com.reds2.school;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Font;

import com.reds2.school.util.Util;

public class Settings implements State{
    public int V=5,beamV=13;
    int[] values = {V,beamV};
    private Rectangle[] Buttons = new Rectangle[2];
    String[] Labels = {"Ship Velocity:","Beam Velocity:"};
    Font font = new Font("h",Font.BOLD,15);
    Settings(){
        for (int i=0;i<Buttons.length;i++) {
            Buttons[i]=new Rectangle(200,100*(i+1)-20,50,25);
        }
    }
    @Override
    public BufferedImage draw() {
        BufferedImage result = new BufferedImage(540, 1080, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(java.awt.Color.white);

        g.setFont(font);

        for (int i = 0;i<Buttons.length; i++){
            g.drawString(Labels[i],50,100*(i+1));
            g.draw(Buttons[i]);
        }

        return result;
    }

    @Override
    public void click(MouseEvent e, Dimension d) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void drag(MouseEvent e, Dimension d) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void m_release(MouseEvent e, Dimension d) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void press(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_DELETE:
                Main.INSTANCE.current = Main.INSTANCE.menu;
        }
        
    }

    @Override
    public void release(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
