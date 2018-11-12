package model.persistence;

import model.ShapeShadingType;
import model.JPaintShapeAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangeShadingTypeCommand implements ICommand {
    private ShapeShadingType shading;
    private ArrayList<JPaintShapeAdapter> selectedShapes;
    private HashMap<JPaintShapeAdapter, ShapeShadingType> initialShapeShadingTypes;
    public ChangeShadingTypeCommand(ShapeShadingType shading, ArrayList<JPaintShapeAdapter> selectedShapes)
    {
        this.shading = shading;
        this.selectedShapes = selectedShapes;
        this.initialShapeShadingTypes = new HashMap<>();
    }
    public void execute()
    {
        for (JPaintShapeAdapter s: this.selectedShapes)
        {
            initialShapeShadingTypes.put(s, s.getJPaintShape().getShapeShadingType());
            s.getJPaintShape().setShapeShadingType(shading);
        }
    }

    public void undo()
    {
        for (JPaintShapeAdapter s: this.initialShapeShadingTypes.keySet())
        {
            ShapeShadingType shading = this.initialShapeShadingTypes.get(s);
            s.getJPaintShape().setShapeShadingType(shading);
        }
    }
}

