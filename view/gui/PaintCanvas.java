package view.gui;

import javax.swing.JPanel;
import java.awt.*;

import java.util.ArrayList;

import model.*;

public class PaintCanvas extends JPanel {
    private Shape shape;    
    private Color primaryColor;    
    private Color secondaryColor;    
    private ShapeShadingType shading;    
    private StartAndEndPointMode mode;    

    public PaintCanvas() {
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setColor(Color color) {
        this.primaryColor = color;
    }

    public void setSecondaryColor(Color color) {
        this.secondaryColor = color;
    }

    public void setShading(ShapeShadingType shading) {
        this.shading = shading;
    }

    public void setMode(StartAndEndPointMode mode) {
        this.mode = mode;
    }

    public Graphics2D getGraphics2D() {
        return (Graphics2D)getGraphics();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        if (shape != null)
        {
            System.out.println(shape + " " + primaryColor + " " + secondaryColor + " " + shading + " " + mode);
            g2d.setPaint(primaryColor);
            g2d.fill(shape);
            g2d.setStroke(new BasicStroke(3)); // set border width
            g2d.setPaint(secondaryColor); // set border color
            g2d.draw(shape); // draw outline
        }
    }
}
