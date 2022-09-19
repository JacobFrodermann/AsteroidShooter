package com.reds2.school;

import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.Dimension;

public interface State {
    BufferedImage draw();
    void click(MouseEvent e,Dimension d);
    void drag(MouseEvent e,Dimension d);
    void m_release(MouseEvent e,Dimension d);
    void press(KeyEvent e);
    void release(KeyEvent e);
}
