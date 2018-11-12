package view.gui;

import model.*;

import java.awt.*;
import javax.swing.JPanel;
import java.util.ArrayList;

public class PaintCanvas extends JPanel 
{
    private ArrayList<JPaintShapeAdapter> shapes; // TODO this shouldt be public

    public PaintCanvas() 
    {
        shapes = new ArrayList<>();
    }

    public void setShapes(ArrayList<JPaintShapeAdapter> shapes)
    {
        this.shapes = shapes;
    }

    public void repaintCanvas(ArrayList<JPaintShapeAdapter> shapes, JPaintShapeAdapter mouseDraggedShape)
    {
        ArrayList<JPaintShapeAdapter> allShapes = new ArrayList<> (shapes);
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

        for (JPaintShapeAdapter s: shapes)
        {
            //System.out.println(shapes.size() + " Drawing shape: " + s.getShape() + " X: " + s.getX() + " y: " + s.getY() + " w: " + s.getWidth() + " h: " + s.getHeight());
            IPaintStrategy strategy;
            switch (s.getJPaintShape().getShapeShadingType())
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
                default:
                    throw new IllegalArgumentException("Invalid ShapeShadingType found when painting");
            }
            strategy.execute();
        }
    }
}
