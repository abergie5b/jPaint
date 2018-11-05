package model.persistence;

import model.*;
import java.util.ArrayList;

public class AddShapeCommand implements ICommand
{
    private ShapeAdapter shape;
    private ArrayList<ShapeAdapter> shapes;
    public AddShapeCommand(ArrayList<ShapeAdapter> shapes, ShapeAdapter shape)
    {
        this.shape = shape;
        this.shapes = shapes;
    }
    public void execute()
    {
        this.shapes.add(this.shape);
    }

    public void undo()
    {
        for (int x=0; x<this.shapes.size(); x++)
        {
            if (this.shapes.get(x) == this.shape)
            {
                this.shapes.remove(x);
            }
        }
    }
}
