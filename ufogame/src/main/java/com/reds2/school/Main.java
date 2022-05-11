package com.reds2.school;
                                         
import javax.imageio.ImageIO;
import javax.swing.JFrame;//                  View
import java.awt.Canvas;//                     View das vorauf man zeichnet
import java.awt.Graphics;//                   View das was zeichnet
import java.awt.Toolkit;//                    View getWith() und Co
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;//       View konzept: zwei Speicher einer wird angezeigt auf dem andereen wind der nächste frame gemahlt siehe Main.java:25
import java.awt.image.BufferedImage;//        Picture
import java.io.IOException;
import java.awt.Dimension;//                  Eine Klasse mit einem x und einem Y Wert
import java.awt.Graphics2D;//                 View das was zeichnet2
import java.awt.RenderingHints;//             View internal
import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;//              logging


public class Main {
    BufferedImage bg;
    public static Main INSTANCE;//            stelt sicher das es nur eine Instanz gibt
	private static final Logger log = LoggerFactory.getLogger(Main.class);
    int skin = 0;
    public static void main(String[] args) throws IOException {
		INSTANCE = new Main();
		INSTANCE.init();
  
        try {
        //Thread.sleep(10l);
            while(true) {
                render(INSTANCE.canvas, INSTANCE.draw(INSTANCE.canvas.getSize()));
                Thread.sleep(1000 / 60);//Führe 60 mal/s Main.draw aus
            }
        } catch (InterruptedException e) {System.out.println("crashed");}
    }

    private JFrame frame;
    private Canvas canvas;
    double width, height;
    State current;
    Settings settings = new Settings();

    Main() throws IOException{
        bg = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("MainBg.png"));
        current = new Menu(new Dimension(bg.getWidth()/3, bg.getHeight()));
    }
    void init(){
        width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame = new JFrame();                                                     
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                     //Wenn geschlossen bennde Programm
        frame.setBackground(Color.WHITE);
        frame.setBounds(0, 0, (int) width, (int) height);                       // setze Anzeigfläche größe auf maximum                     
        frame.setResizable(false);            
        canvas = new Canvas();
        frame.add(canvas);                                                        //füge Malfläche zu Anzeige hinzu
        canvas.addKeyListener(new KeyListener() {                                 //Tastatur abfrage erstellen
            @Override
            public void keyPressed(KeyEvent e) {
                current.press(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {                                  //Methoden werden aufgerufen wenn Taste gedrückt losgelassen oder getippt 
                current.release(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
		});
        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}//werde ich nicht nutzen

            @Override
            public void mousePressed(MouseEvent e) {
                current.click(e, new Dimension((int) width,(int) height));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                current.m_release(e, new Dimension((int) width,(int) height));
            }
            
        });
        frame.setVisible(true);
		canvas.createBufferStrategy(2);
		canvas.requestFocus();
        log.debug("created Frame");
    }

    public static void render(Canvas canvas, BufferedImage img) {
		BufferStrategy bs = canvas.getBufferStrategy();
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		g.dispose();
		bs.show();
	}

    public BufferedImage draw(Dimension size) {
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// hier kann man wie in SAS nur durch awt auf eine Bild im Buffer mahlen das in einer 1/60 s angezeigt wird
        g.drawImage(bg, 0, 0, size.width, size.height,null);
        g.drawImage(current.draw(),(size.width-size.height/2)/2,0,size.height/2,size.height,null);
        
        
        //g.setColor(Color.red);
        //g.fillRect(0,10,10,10);
		return result;
	}
}