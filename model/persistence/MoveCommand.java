package model.persistence;

import model.*;
import java.util.ArrayList;

public class MoveCommand implements ICommand
{
    private ShapeAdapter to;
    private ShapeAdapter from;
    private ArrayList<ShapeAdapter> shapes;
    public MoveCommand(ArrayList<ShapeAdapter> shapes, ShapeAdapter to, ShapeAdapter from)
    {
        this.shapes = shapes;
        this.to = to;
        this.from = from;
    }
    public void execute()
    {
        for (int x=0; x<shapes.size(); x++)
        {
            if (shapes.get(x) == to)
            {
                shapes.remove(x);
            }
        }
        shapes.add(from);
    }

    public void undo()
    {
        for (int x=0; x<shapes.size(); x++)
        {
            if (shapes.get(x) == from)
            {
                shapes.remove(x);
            }
        }
        shapes.add(to);
    }
}
