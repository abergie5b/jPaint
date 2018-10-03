package model.interfaces;

import model.*;
import java.util.ArrayList;

public interface IApplicationState {
    void undo();

    void redo();

    void delete();

    void copy();

    void paste();

    void setSelectedShapes(ArrayList<StateModelAdapter> shapes);

    void setActiveShape();

    void setActivePrimaryColor();

    void setActiveSecondaryColor();

    void setActiveShadingType();

    void setActiveStartAndEndPointMode();

    ShapeType getActiveShapeType();

    ShapeColor getActivePrimaryColor();

    ShapeColor getActiveSecondaryColor();

    ShapeShadingType getActiveShapeShadingType();

    StartAndEndPointMode getActiveStartAndEndPointMode();
}
