package view.gui;

import javax.swing.JPanel;
import java.awt.*;

import java.util.ArrayList;

public class PaintCanvas extends JPanel {
    private Shape shape;    

    public PaintCanvas() {
    }

    public void setShape(Shape s) {
        this.shape = s;
    }

    public Graphics2D getGraphics2D() {
        return (Graphics2D)getGraphics();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.BLUE);
        g2d.setBackground(Color.BLUE);
        if (shape != null)
            g2d.draw(shape);
    }
}
