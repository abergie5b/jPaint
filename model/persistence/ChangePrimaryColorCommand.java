package model.persistence;

import model.ShapeColor;
import model.JPaintShapeAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class ChangePrimaryColorCommand implements ICommand {
    private ShapeColor color;
    private ArrayList<JPaintShapeAdapter> selectedShapes;
    private HashMap<JPaintShapeAdapter, ShapeColor> initialShapeColors;

    public ChangePrimaryColorCommand(ShapeColor color, ArrayList<JPaintShapeAdapter> selectedShapes)
    {
        this.color = color;
        this.selectedShapes = selectedShapes;
        this.initialShapeColors = new HashMap<>();
    }

    public void execute()
    {
        for (JPaintShapeAdapter s: this.selectedShapes)
        {
            initialShapeColors.put(s, s.getJPaintShape().getPrimaryShapeColor());
            s.getJPaintShape().setPrimaryColor(color);
        }
    }

    public void undo()
    {
        for (JPaintShapeAdapter s: this.initialShapeColors.keySet())
        {
            ShapeColor color = this.initialShapeColors.get(s);
            s.getJPaintShape().setPrimaryColor(color);
        }
    }
}
