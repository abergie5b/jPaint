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
    private ArrayList<StateModelAdapter> shapes;

    public PaintCanvas() 
    {
        shapes = new ArrayList<StateModelAdapter>();
    }

    public void addMouseListeners(MouseEventListener mouseEventListener) {
        this.addMouseMotionListener(mouseEventListener);
        this.addMouseListener(mouseEventListener);
    }

    public void updateShapeSettings(StateModelAdapter stateModel) {
        this.shape = stateModel.shape;
        this.primaryColor = stateModel.primaryColor;
        this.secondaryColor = stateModel.secondaryColor;
        this.shading = stateModel.shapeShadingType;
        this.mode = stateModel.startAndEndPointMode;
    }

    public Shape getShapeFromBuffer(Point point) {
        Shape _shape = null;
        for (StateModelAdapter s: shapes)
        {
            if (s.shape.contains(point))
            {
                _shape = s.shape;
            }
        }
        return _shape;
    }

    public void addShapeAttribute(StateModelAdapter _shape) {
        shapes.add(_shape);
    }

    private Graphics2D getGraphics2D() {
        return (Graphics2D)getGraphics();
    }
    
    public void paintComponent(Graphics g) {
        System.out.println(shape + " " + primaryColor + " " + secondaryColor + " " + shading + " " + mode);
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        if (shape == null)
        {
            return;
        }
        g2d.setStroke(new BasicStroke(3));

        for (StateModelAdapter s: shapes)
        {
            if (s.shapeShadingType == ShapeShadingType.FILLED_IN)
            {
                g2d.setPaint(s.primaryColor);
                g2d.fill(s.shape);
            }
            else if (s.shapeShadingType == ShapeShadingType.OUTLINE)
            {
                g2d.setPaint(s.secondaryColor);
            }
            else if (s.shapeShadingType == ShapeShadingType.OUTLINE_AND_FILLED_IN)
            {
                g2d.setPaint(s.primaryColor);
                g2d.fill(s.shape);
                g2d.setPaint(s.secondaryColor);
            }
            g2d.draw(s.shape);
        }
    }
}
