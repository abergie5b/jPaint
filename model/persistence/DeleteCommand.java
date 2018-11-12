package model.persistence;

import model.*;
import java.util.ArrayList;

public class DeleteCommand implements ICommand
{
    private ArrayList<JPaintShapeAdapter> shapes;
    private ArrayList<JPaintShapeAdapter> selectedShapes;
    public DeleteCommand(ArrayList<JPaintShapeAdapter> shapes, ArrayList<JPaintShapeAdapter> selectedShapes)
    {
        this.shapes = shapes;
        this.selectedShapes = selectedShapes;
    }
    public void execute()
    {
        for (int x=0; x<shapes.size(); x++)
        {
            for (int y=0; y<selectedShapes.size(); y++)
            {
                if (shapes.get(x) == selectedShapes.get(y))
                {
                    shapes.remove(x);
                }
            }
        }
    }

    public void undo()
    {
        for (JPaintShapeAdapter s: selectedShapes)
        {
            this.shapes.add(s);
        }
    }
}
