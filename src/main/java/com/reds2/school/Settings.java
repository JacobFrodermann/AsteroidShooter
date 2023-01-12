package com.reds2.school;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import com.reds2.school.util.Util;
import lombok.Getter;
import java.awt.Rectangle;
import java.awt.Font;

public class Settings implements State{
    public int v=5,beamV=20,cooldown=5,inv=12,red=6,lives=2,astoids=4,particlesnumber=1;
    int[] values = {v,beamV,cooldown,inv,red,lives,astoids,particlesnumber};
    private static int[] MAX_VALUES = {10,30,25,20,10,4,8,2};
    private static int[] MIN_VALUES = {2 ,10,5 ,5 ,0 ,0,1,1};
    private Rectangle[] buttons = new Rectangle[7], switches = new Rectangle[1];
    String[] Labels = {"Ship Velocity:","Beam Velocity:","Beam Cooldown:","invincibility time:","Difficultyreduction on Death :","Extra Lives:","Asteroids:","Particles:"};
    Font font = new Font("h",Font.BOLD,15);
    BufferedImage done = Util.load("Done"), bg = Util.load("Settings_BG");
    @Getter int ScreenX = 720, ScreenY = 1080;

    Rectangle doneHitbox = new Rectangle(285,970,150,60);
    public Boolean particles = true;

    Settings(){
        font.deriveFont(Font.CENTER_BASELINE, 0f);
        font.deriveFont(Font.TYPE1_FONT, 0f);

        for (int i=0;i<buttons.length;i++) {
            buttons[i]=new Rectangle(480,40*(i+1)+605,50,35);
        }
    
        for (int i=buttons.length;i<buttons.length+switches.length;i++) {
            switches[i-buttons.length]=new Rectangle(480,40*(i+1)+605,50,35);
        }
    }

    public BufferedImage draw() {
        BufferedImage result = new BufferedImage(ScreenX, ScreenY, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(java.awt.Color.black);

        g.drawImage(bg, 0, 0, 720, 1080, null);

        g.setFont(font);

        for (int i = 0;i<buttons.length; i++){
            g.drawString(Labels[i],150,40*(i+1)+630);
            g.drawString(String.valueOf(values[i]),500,40*(i+1)+630);
        }
        for (int i=buttons.length;i<buttons.length+switches.length;i++) {
            g.drawString(Labels[i],150,40*(i+1)+630);
            if (particles) {
                g.drawString("True" , 485, 40*(i+1)+630);
            } else {
                g.drawString("False", 485, 40*(i+1)+630);
            }
        }

        g.drawImage(done,doneHitbox.x,doneHitbox.y,null);

        return result;
    }

    @Override
    public void click(MouseEvent e, Dimension d) {
        int x = e.getX()-(960-ScreenX/2);
		int y = e.getY();

        java.awt.Point p = new java.awt.Point(x,y);

        if (doneHitbox.contains(p)) {
            Main.INSTANCE.current = Main.INSTANCE.menu;
        }

        for (int i = 0;i<buttons.length;i++){
            if (buttons[i].contains(p)){
                if(e.getButton() == 1){
                    values[i]++;
                } else {
                    values[i]--;
                }
            }
        }
        for (int i = buttons.length;i<switches.length+buttons.length;i++){
            if (switches[i-buttons.length].contains(p)){
                if(e.getButton() == 1){
                    values[i]++;
                } else {
                    values[i]--;
                }
            }
        }

        for (int i = 0;i<values.length;i++){
            values[i] %= MAX_VALUES[i];
            if (values[i] == MIN_VALUES[i]-1) {
                values[i] = MAX_VALUES[i];
            }
        }

        v = values[0];
        beamV = values[1];
        cooldown = values[2];
        inv = values[3];
        red = values[4];
        lives = values[5];
        astoids = values[6];
        particlesnumber  = values[7];
        particles = 1 == particlesnumber;
    }

    @Override
    public void drag(MouseEvent e, Dimension d) {}

    @Override
    public void m_release(MouseEvent e, Dimension d) {}

    @Override
    public void press(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_DELETE:
            File f = new File("Settings.dat");
            try {Writer writer = new BufferedWriter(new FileWriter(f));
            for (int v : values) writer.write(String.valueOf(v*132+4)+"\n");
            writer.close();} catch (Throwable t){}
            
            Main.INSTANCE.current = Main.INSTANCE.menu;
        }
        
    }

    @Override
    public void release(KeyEvent e) {}
}
