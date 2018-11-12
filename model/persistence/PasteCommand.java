package model.persistence;

import model.*;
import java.util.ArrayList;

import java.awt.*;

public class PasteCommand implements ICommand
{
    private ArrayList<JPaintShapeAdapter> shapes;
    private ArrayList<JPaintShapeAdapter> selectedShapes;
    private ArrayList<JPaintShapeAdapter> pastedShapes;
    public PasteCommand(ArrayList<JPaintShapeAdapter> shapes, ArrayList<JPaintShapeAdapter> selectedShapes)
    {
        this.shapes = shapes;
        this.selectedShapes = selectedShapes;
        this.pastedShapes = new ArrayList<>();
    }

    public void execute()
    {
        for (JPaintShapeAdapter s: selectedShapes)
        {
            JPaintShape shape = new JPaintShape(s.getJPaintShape().getShapeType(),
                                                s.getJPaintShape().getPrimaryShapeColor(),
                                                s.getJPaintShape().getSecondaryShapeColor(),
                                                s.getJPaintShape().getShapeShadingType(),
                                                s.getJPaintShape().getStartAndEndPointMode()
            );
            JPaintShapeAdapter newShape = new JPaintShapeAdapter(shape, new Dimensions(new Point(0, 0), new Point(s.getWidth(), s.getHeight())));
            pastedShapes.add(newShape);
            AddShapeCommand addShape = new AddShapeCommand(shapes, newShape);
            addShape.execute();
        }
    }

    public void undo()
    {
        for (int x=0; x<this.shapes.size(); x++)
        {
            for (int y=0; y<this.pastedShapes.size(); y++)
            {
                if (this.shapes.get(x) == this.pastedShapes.get(y))
                {
                    this.shapes.remove(x);
                }
            }
        }
    }
}
