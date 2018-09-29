package view.gui;

import java.awt.event.*;

public interface IMouseEventListener {
    void mousePressed(MouseEvent evt);
    void mouseDragged(MouseEvent evt);
    void mouseReleased(MouseEvent evt);
}
