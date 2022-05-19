package com.reds2.school;

import com.reds2.school.util.Util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;



public class GameMenu extends BufferedImage {
    private BufferedImage bg = Util.load("GameMenuBG"),home = Util.load("HomeB"),restart = Util.load("restart");
    private Font font = new Font("h",1,25);
    GameMenu(double time){
        super(300,100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) this.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawImage(bg, 0, 0, null);

        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString("Game Over", 75, 30);
    }
}
