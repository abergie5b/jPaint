package view.gui;

import model.*;
import view.gui.MouseEventListener;

import java.awt.*;
import javax.swing.JPanel;
import java.util.ArrayList;

public class PaintCanvas extends JPanel 
{
    private Shape shape;    
    private ShapeAdapter mouseDraggedShape;    
    private Color primaryColor;    
    private Color secondaryColor;    
    private ShapeShadingType shading;    
    private StartAndEndPointMode mode;    
    public ArrayList<ShapeAdapter> shapes; // TODO this shouldt be public
    private ArrayList<ShapeAdapter> shapeHistory;

    public PaintCanvas() 
    {
        shapes = new ArrayList<ShapeAdapter>();
        shapeHistory = new ArrayList<ShapeAdapter>();
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
        // TODO implement Command Pattern
        if (getNumberOfShapeHistory() > 0) 
        {
            ShapeAdapter s = shapeHistory.get(getNumberOfShapeHistory() - 1);
            shapeHistory.remove(getNumberOfShapeHistory() - 1);
            shapes.add(s);
        }
        repaint();
    }
    public void undo() 
    {
        // TODO implement Command Pattern
        if (getNumberOfShapes() > 0) 
        {
            ShapeAdapter s = shapes.get(getNumberOfShapes() - 1);
            shapes.remove(getNumberOfShapes() - 1);
            shapeHistory.add(s);
        }
        repaint();
    }

    public ArrayList<ShapeAdapter> getShapesinSelection(Rectangle selection)
    {
        ArrayList<ShapeAdapter> selectedShapes = new ArrayList<ShapeAdapter>();
        for (ShapeAdapter s: shapes) 
        {
            if (s.shape.intersects(selection))
            {
                selectedShapes.add(s);
                System.out.println("Selected " + s.shape);
            }
        }
        return selectedShapes;
    }

    public void removeShapeFromBuffer(ShapeAdapter shape) 
    {
        for (int x=0; x<shapes.size(); x++)
        {
            ShapeAdapter s = shapes.get(x);
            if (shape.equals(s))
            {
                System.out.println("Removing shape: " + s);
                shapes.remove(x);
            }
        }
    }

    public void deleteShapes(ArrayList<ShapeAdapter> selectedShapes)
    {
        for (ShapeAdapter ss: selectedShapes)
        {
            this.removeShapeFromBuffer(ss);
        }
        this.repaint();
    }

    public ShapeAdapter getShapeFromBuffer(Point point) 
    {
        ShapeAdapter _shape = null;
        for (ShapeAdapter s: shapes)
        {
            if (s.shape.contains(point))
            {
                _shape = s;
            }
        }
        return _shape;
    }

    public void addShapeAttribute(ShapeAdapter _shape) 
    {
        shapes.add(_shape);
    }

    public void setMouseDraggedShape(ShapeAdapter _shape) 
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

        ArrayList<ShapeAdapter> allShapes = new ArrayList<ShapeAdapter> (shapes);
        if (this.mouseDraggedShape != null)
        {
            allShapes.add(this.mouseDraggedShape);
        }
        for (ShapeAdapter s: allShapes)
        {
            System.out.println(allShapes.size() + " Drawing shape: " + s.shape + " X: " + s.x + " y: " + s.y + " w: " + s.width + " h: " + s.height);
            IPaintStrategy strategy = null;
            switch (s.shapeShadingType)
            {
                case FILLED_IN:
                    strategy = new FilledInPaintStrategy(g2d, s);
                    break;
                case OUTLINE:
                    strategy = new OutlinePaintStrategy(g2d, s);
                    break;
                case OUTLINE_AND_FILLED_IN:
                    strategy = new OutlineAndFilledInPaintStrategy(g2d, s);
                    break;
            }
            if (strategy != null)
            {
                strategy.execute();
            }
        }
        this.mouseDraggedShape = null;
    }
}
