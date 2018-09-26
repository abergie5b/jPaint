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
    private ArrayList<Shape> shapes;

    public PaintCanvas() {
        shapes = new ArrayList<Shape>();
    }

    public Shape getShapeFromBuffer(Point point) {
        Shape _shape = null;
        for (Shape s: shapes)
        {
            if (s.contains(point))
            {
                _shape = s;
            }
        }
        return _shape;
    }

    public boolean pointContainsShape(Point point) {
        for (Shape s: shapes)
        {
            if (s.contains(point))
            {
                return true;
            }
        }
        return false;
    }

    private void addShape(Shape shape) {
        shapes.add(shape);
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
        System.out.println(shape + " " + primaryColor + " " + secondaryColor + " " + shading + " " + mode);
        //super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        if (shape == null)
        {
            return;
        }
        g2d.setStroke(new BasicStroke(3));

        if (shading == ShapeShadingType.FILLED_IN)
        {
            g2d.setPaint(primaryColor);
            g2d.fill(shape);
        }
        else if (shading == ShapeShadingType.OUTLINE)
        {
            g2d.setPaint(secondaryColor);
        }
        else if (shading == ShapeShadingType.OUTLINE_AND_FILLED_IN)
        {
            g2d.setPaint(primaryColor);
            g2d.fill(shape);
            g2d.setPaint(secondaryColor);
        }
        g2d.draw(shape);
        addShape(shape);
    }
}
