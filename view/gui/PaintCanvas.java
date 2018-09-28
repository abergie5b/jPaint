package view.gui;

import javax.swing.JPanel;
import java.awt.*;

import java.util.ArrayList;

import model.*;
import controller.MouseEventListener;

public class PaintCanvas extends JPanel 
{
    private Shape shape;    
    private StateModelAdapter mouseDraggedShape;    
    private Color primaryColor;    
    private Color secondaryColor;    
    private ShapeShadingType shading;    
    private StartAndEndPointMode mode;    
    private ArrayList<StateModelAdapter> shapes;
    private ArrayList<StateModelAdapter> shapeHistory;

    public PaintCanvas() 
    {
        shapes = new ArrayList<StateModelAdapter>();
        shapeHistory = new ArrayList<StateModelAdapter>();
    }

    private int getNumberOfShapes() {
        return shapes.size();
    }

    private int getNumberOfShapeHistory() {
        return shapeHistory.size();
    }

    public void redo() {
        if (getNumberOfShapeHistory() > 0) {
            StateModelAdapter s = shapeHistory.get(getNumberOfShapeHistory() - 1);
            shapeHistory.remove(getNumberOfShapeHistory() - 1);
            shapes.add(s);
        }
        repaint();
    }

    public void undo() {
        if (getNumberOfShapes() > 0) {
            StateModelAdapter s = shapes.get(getNumberOfShapes() - 1);
            shapes.remove(getNumberOfShapes() - 1);
            shapeHistory.add(s);
        }
        repaint();
    }

    public void addMouseListeners(MouseEventListener mouseEventListener) {
        this.addMouseMotionListener(mouseEventListener);
        this.addMouseListener(mouseEventListener);
    }

    public StateModelAdapter getShapeFromBuffer(Point point) {
        StateModelAdapter _shape = null;
        for (StateModelAdapter s: shapes)
        {
            if (s.shape.contains(point))
            {
                _shape = s;
            }
        }
        return _shape;
    }

    public void addShapeAttribute(StateModelAdapter _shape) {
        shapes.add(_shape);
    }

    public void setTempShape(StateModelAdapter _shape) {
        this.mouseDraggedShape = _shape;
    }

    private Graphics2D getGraphics2D() {
        return (Graphics2D)getGraphics();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(3));

        ArrayList<StateModelAdapter> allShapes = new ArrayList<StateModelAdapter> (shapes);
        if (this.mouseDraggedShape != null)
        {
            allShapes.add(this.mouseDraggedShape);
        }
        for (StateModelAdapter s: allShapes)
        {
            System.out.println("Drawing shape: " + s.shape + " " + s.x + " " + s.y + " " + s.width + " " + s.height);
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
        this.mouseDraggedShape = null;
    }
}
