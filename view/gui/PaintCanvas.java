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

    public void setMouseDraggedShape(ShapeAdapter _shape) 
    {
        this.mouseDraggedShape = _shape;
    }

    private Graphics2D getGraphics2D() 
    {
        return (Graphics2D)getGraphics();
    }

    public void setShapes(ArrayList<ShapeAdapter> shapes)
    {
        this.shapes = shapes;
    }

    public void repaintCanvas(ArrayList<ShapeAdapter> shapes)
    {
        this.setShapes(shapes);
        this.repaint();
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
