package view.gui;

import javax.swing.JPanel;
import java.awt.*;

import java.util.ArrayList;

import model.*;
import view.gui.MouseEventListener;

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

    private int getNumberOfShapes() 
    {
        return shapes.size();
    }

    private int getNumberOfShapeHistory() 
    {
        return shapeHistory.size();
    }

    public void redo() 
    {
        if (getNumberOfShapeHistory() > 0) 
        {
            StateModelAdapter s = shapeHistory.get(getNumberOfShapeHistory() - 1);
            shapeHistory.remove(getNumberOfShapeHistory() - 1);
            shapes.add(s);
        }
        repaint();
    }

    public void undo() 
    {
        if (getNumberOfShapes() > 0) 
        {
            StateModelAdapter s = shapes.get(getNumberOfShapes() - 1);
            shapes.remove(getNumberOfShapes() - 1);
            shapeHistory.add(s);
        }
        repaint();
    }

    public void addMouseListeners(MouseEventListener mouseEventListener) 
    {
        this.addMouseMotionListener(mouseEventListener);
        this.addMouseListener(mouseEventListener);
    }

    public ArrayList<StateModelAdapter> getShapesinSelection(Rectangle selection)
    {
        ArrayList<StateModelAdapter> selectedShapes = new ArrayList<StateModelAdapter>();
        for (StateModelAdapter s: shapes) 
        {
            if (s.shape.intersects(selection))
            {
                selectedShapes.add(s);
                System.out.println("Selected " + s.shape);
            }
        }
        return selectedShapes;
    }

    public void removeShapeFromBuffer(StateModelAdapter shape) 
    {
        for (int x=0; x<shapes.size(); x++)
        {
            StateModelAdapter s = shapes.get(x);
            if (shape.equals(s))
            {
                System.out.println("Removing shape: " + s);
                shapes.remove(x);
            }
        }
    }

    public void deleteShapes(ArrayList<StateModelAdapter> selectedShapes)
    {
        for (StateModelAdapter ss: selectedShapes)
        {
            this.removeShapeFromBuffer(ss);
        }
        this.repaint();
    }

    public StateModelAdapter getShapeFromBuffer(Point point) 
    {
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

    public void addShapeAttribute(StateModelAdapter _shape) 
    {
        shapes.add(_shape);
    }

    public void setTempShape(StateModelAdapter _shape) 
    {
        this.mouseDraggedShape = _shape;
    }

    private Graphics2D getGraphics2D() 
    {
        return (Graphics2D)getGraphics();
    }
    
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(2));

        ArrayList<StateModelAdapter> allShapes = new ArrayList<StateModelAdapter> (shapes);
        if (this.mouseDraggedShape != null)
        {
            allShapes.add(this.mouseDraggedShape);
        }
        for (StateModelAdapter s: allShapes)
        {
            System.out.println(allShapes.size() + " Drawing shape: " + s.shape + " X: " + s.x + " y: " + s.y + " w: " + s.width + " h: " + s.height);
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
