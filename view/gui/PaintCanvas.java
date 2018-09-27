package view.gui;

import javax.swing.JPanel;
import java.awt.*;

import java.util.ArrayList;

import model.*;
import controller.MouseEventListener;

public class PaintCanvas extends JPanel 
{
    private Shape shape;    
    private Color primaryColor;    
    private Color secondaryColor;    
    private ShapeShadingType shading;    
    private StartAndEndPointMode mode;    
    private ArrayList<Shape> shapes;

    public PaintCanvas() 
    {
        shapes = new ArrayList<Shape>();
    }

    public void addMouseListeners(MouseEventListener mouseEventListener) {
        this.addMouseMotionListener(mouseEventListener);
        this.addMouseListener(mouseEventListener);
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void updateShapeSettings(StateModelAdapter stateModel) {
        this.primaryColor = stateModel.primaryColor;
        this.secondaryColor = stateModel.secondaryColor;
        this.shading = stateModel.shapeShadingType;
        this.mode = stateModel.startAndEndPointMode;
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

    private void addShape(Shape _shape) {
        shapes.add(_shape);
    }

    private Graphics2D getGraphics2D() {
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
