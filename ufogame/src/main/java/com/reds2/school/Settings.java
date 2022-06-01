package com.reds2.school;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.reds2.school.util.Util;

import java.awt.Rectangle;
import java.awt.Font;

public class Settings implements State{
    public int V=5,beamV=13,cooldown=15,inv=12,red=6,lives=2;
    int[] values = {V,beamV,cooldown,inv,red,lives};
    private static int[] MAX_VALUES = {10,20,25,20,10,4};
    private Rectangle[] Buttons = new Rectangle[6];
    String[] Labels = {"Ship Velocity:","Beam Velocity:","Beam Cooldown:","invincibility time:","Difficultyreduction on Death :","Extra Lives:"};
    Font font = new Font("h",Font.BOLD,15);
    BufferedImage done = Util.load("Done");

    Rectangle doneHitbox = new Rectangle(350,900,150,60);

    Settings(){
        for (int i=0;i<Buttons.length;i++) {
            Buttons[i]=new Rectangle(300,100*(i+1)-20,50,25);
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
            g.drawString(String.valueOf(values[i]),320,100*(i+1));
        }

        g.drawImage(done,350,900,null);

        return result;
    }

    @Override
    public void click(MouseEvent e, Dimension d) {
        int x = (e.getX()-(d.width-d.height/2)/2)*1080/d.height;
		int y = e.getY()*1080/d.height;

        if (doneHitbox.contains(new java.awt.Point(x,y))) {
            Main.INSTANCE.current = Main.INSTANCE.menu;
        }

        for (int i = 0;i<Buttons.length;i++){
            if (Buttons[i].contains(new java.awt.Point(x,y))){
                if(e.getButton() == 1){
                    values[i]++;
                } else {
                    values[i]--;
                }
            }
        }

        for (int i = 0;i<values.length;i++){
            values[i] %= MAX_VALUES[i];
            if (values[i] == 0) {
                values[i] = MAX_VALUES[i];
            }
        }

        V = values[0];
        beamV = values[1];
        cooldown = values[2];
        inv = values[3];
        red = values[4];
        lives = values[5];
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
