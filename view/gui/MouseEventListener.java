package view.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MouseEventListener extends MouseInputAdapter {

  @Override
  public void mousePressed(MouseEvent evt) {
     int startX = evt.getX();
     int startY = evt.getY();
     System.out.println("(" + startX + "," + startY + ")");
  }

  @Override
  public void mouseDragged(MouseEvent evt) {
     int endX = evt.getX();
     int endY = evt.getY();
     System.out.println("(" + endX + "," + endY + ")");
     //repaint();  // Called back paintComponent()
  }

  @Override
     public void mouseReleased(MouseEvent evt) {
     int endX = evt.getX();
     int endY = evt.getY();
     System.out.println("(" + endX + "," + endY + ")");
     //repaint();  // Called back paintComponent()
  }
}

