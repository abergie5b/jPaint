package model.persistence;

import model.*;
import java.util.ArrayList;

public class MoveCommand implements ICommand
{
    private JPaintShapeAdapter to;
    private JPaintShapeAdapter from;
    private ArrayList<JPaintShapeAdapter> shapes;
    public MoveCommand(ArrayList<JPaintShapeAdapter> shapes, JPaintShapeAdapter from, JPaintShapeAdapter to)
    {
        this.shapes = shapes;
        this.from = from;
        this.to = to;
    }
    public void execute()
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

    public void undo()
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
}
