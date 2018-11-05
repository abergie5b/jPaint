package model.persistence;

import model.*;
import java.util.ArrayList;

import java.awt.geom.*;
import java.awt.*;

public class PasteCommand implements ICommand
{
    private ArrayList<ShapeAdapter> shapes;
    private ArrayList<ShapeAdapter> selectedShapes;
    public PasteCommand(ArrayList<ShapeAdapter> shapes, ArrayList<ShapeAdapter> selectedShapes)
    {
        this.shapes = shapes;
        this.selectedShapes = selectedShapes;
    }

    public void execute()
    {
        for (ShapeAdapter s: selectedShapes)
        {
            ShapeAdapter newShape = new ShapeAdapter(s.shapeType,
                                                     s.primaryShapeColor,
                                                     s.secondaryShapeColor,
                                                     s.shapeShadingType,
                                                     s.startAndEndPointMode
            );
            newShape.setShape(new Dimensions(new Point(0, 0), new Point(s.getWidth(), s.getHeight())));
            AddShapeCommand addShape = new AddShapeCommand(shapes, newShape);
            addShape.execute();
        }
    }

    public void undo()
    {
        for (int x=0; x<this.shapes.size(); x++)
        {
            for (int y=0; y<this.selectedShapes.size(); y++)
            {
                if (this.shapes.get(x) == this.selectedShapes.get(y))
                {
                    this.shapes.remove(x);
                }
            }
        }
    }
}
