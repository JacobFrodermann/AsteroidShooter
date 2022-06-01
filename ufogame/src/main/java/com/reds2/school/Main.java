package com.reds2.school;
                                         
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import com.reds2.school.enc.Encryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Main {
    BufferedImage bg;
    public static Main INSTANCE;
	private static final Logger log = LoggerFactory.getLogger(Main.class);
    int skin = 0;
    public Dimension d;

    public static void main(String[] args) throws IOException, InterruptedException {
		INSTANCE = new Main();
		INSTANCE.init();
        while(true) {
            render(INSTANCE.canvas, INSTANCE.draw(INSTANCE.canvas.getSize()));
            try{Thread.sleep(1000 / 60-Main.INSTANCE.game.frameTime);}catch(Exception e){}//Führe 60 mal/s Main.draw aus
        }
    }

    private JFrame frame;
    private Canvas canvas;
    double width, height;
    State current;
    Settings settings = new Settings();
    Menu menu;
    Game game;

    Main() throws IOException{
        bg = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("MainBg.png"));
        d = new Dimension(bg.getWidth()/3, bg.getHeight());
        menu = new Menu(d);
        current = menu;
    }
    void init(){
        game = new Game();
        width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame = new JFrame(); 
        frame.setIconImage(game.ship[0]);                                                    
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

    int loadScore() throws InvalidKeyException, NoSuchAlgorithmException, IOException{
        BufferedReader reader = null;
        try{
            File f = new File("Highscore.txt");
            reader = new BufferedReader(new FileReader(f));
            int value = Integer.valueOf(reader.readLine());
            String timestap = reader.readLine(),mac = reader.readLine();
        
            if (Encryption.getString(String.valueOf(value).getBytes(), timestap).equals(mac)) {
                reader.close();
                return value;
            }
        } catch (Exception e) {
        } finally {
            if(reader != null) reader.close();
        }

        FileWriter w = new FileWriter("Highscore.txt");
        w.write("0\n"+System.currentTimeMillis()+"\n"+Encryption.getString("0".getBytes(), String.valueOf(System.currentTimeMillis())));
        w.close();
        return 0;
    }
    void saveHighscore(int score){
        BufferedWriter writer = null;
        try{
            File f = new File("Highscore.txt");
            long t = System.currentTimeMillis();
            writer = new BufferedWriter(new FileWriter(f));
            writer.write(String.valueOf(score)+"\n");
            writer.write(String.valueOf(t)+"\n");
            writer.write(Encryption.getString(String.valueOf(score).getBytes(), String.valueOf(t)));
            writer.write("\nby "+System.getProperty("user.name"));
        } catch (Exception e ){}
        try{writer.close();}catch(Exception e){}
    }

}