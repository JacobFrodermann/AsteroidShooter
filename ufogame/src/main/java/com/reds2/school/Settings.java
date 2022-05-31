package com.reds2.school;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Settings implements State{
    public int xV=2, yV=2,asteriods=1,beamV=13;
    String name="Test";
    @Override
    public BufferedImage draw() {
        BufferedImage result = new BufferedImage(540, 1080, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

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
