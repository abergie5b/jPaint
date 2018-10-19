package view.gui;

import model.*;
import view.gui.MouseEventListener;

import java.awt.*;
import javax.swing.JPanel;
import java.util.ArrayList;

public class PaintCanvas extends JPanel 
{
    private ArrayList<ShapeAdapter> shapes; // TODO this shouldt be public

    public PaintCanvas() 
    {
        shapes = new ArrayList<ShapeAdapter>();
    }

    private Graphics2D getGraphics2D() 
    {
        return (Graphics2D)getGraphics();
    }

    public void setShapes(ArrayList<ShapeAdapter> shapes)
    {
        this.shapes = shapes;
    }

    public void repaintCanvas(ArrayList<ShapeAdapter> shapes, ShapeAdapter mouseDraggedShape)
    {
        ArrayList<ShapeAdapter> allShapes = new ArrayList<ShapeAdapter> (shapes);
        if (mouseDraggedShape != null)
        {
            allShapes.add(mouseDraggedShape);
        }

        this.setShapes(allShapes);
        this.repaint();
    }
    
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(2));

        for (ShapeAdapter s: shapes)
        {
            System.out.println(shapes.size() + " Drawing shape: " + s.shape + " X: " + s.x + " y: " + s.y + " w: " + s.width + " h: " + s.height);
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
    }
}
