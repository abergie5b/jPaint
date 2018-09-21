package view.gui;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.geom.*;

import java.util.ArrayList;

public class PaintCanvas extends JPanel {
    private ArrayList<Shape> shapes;    

    public PaintCanvas() {
        shapes = new ArrayList<Shape>();
        MouseEventListener mouseListener = new MouseEventListener();
        this.addMouseMotionListener(mouseListener);
    }

    public void addShape(Shape s) {
        shapes.add(s);
    }

    public Graphics2D getGraphics2D() {
        return (Graphics2D)getGraphics();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.BLUE);
        g2d.setBackground(Color.BLUE);
        for (Shape s: shapes)
        {
            g2d.draw(s);
        }
    }

    private class MouseEventListener extends MouseInputAdapter {
        private int mouseX;
        private int mouseY;
        public MouseEventListener() 
        { 
            this.mouseX = 0;
            this.mouseY = 0;
        } 
        @Override
        public void mousePressed(MouseEvent evt) {
            int startX = evt.getX();
            int startY = evt.getY();
            mouseX = startX;
            mouseY = startY;
        }
        @Override
        public void mouseDragged(MouseEvent evt) {
            int endX = evt.getX();
            int endY = evt.getY();
            addShape(new Line2D.Double(mouseX, mouseY, endX, endY));
            System.out.println("X: " + mouseX + " Y: " + mouseY + " endY: " + endX + " endY: " + endY);
            mouseX = endX;
            mouseY = endY;
            repaint();
        }
        @Override
        public void mouseReleased(MouseEvent evt) {
            int endX = evt.getX();
            int endY = evt.getY();
            mouseX = endX;
            mouseY = endY;
            repaint();
        }

    }
}
