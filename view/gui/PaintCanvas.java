package view.gui;

import javax.swing.JPanel;
import java.awt.*;

import java.util.ArrayList;

import model.*;

public class PaintCanvas extends JPanel {
    private Shape shape;    
    private ShapeColor color;    
    private ShapeShadingType shading;    
    private StartAndEndPointMode mode;    

    public PaintCanvas() {
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setColor(ShapeColor color) {
        this.color = color;
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
        g2d.setColor(Color.BLUE);
        g2d.setBackground(Color.BLUE);
        if (shape != null)
            g2d.draw(shape);
    }
}
