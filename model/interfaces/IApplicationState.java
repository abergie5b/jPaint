package model.interfaces;

import model.*;
import java.util.ArrayList;
import java.awt.*;

public interface IApplicationState 
{
    void repaint();
    JPaintShapeAdapter getDraggedShape();
    JPaintShapeAdapter getClickedShape();
    void addShape(JPaintShapeAdapter _shape);
    void move(JPaintShapeAdapter to, JPaintShapeAdapter from);
    void undo();
    void redo();
    void copy();
    void paste();
    void delete();
    void setClickedShape(Point point);
    void setDraggedShape(JPaintShapeAdapter shape);
    void setSelectedShape();
    void setSelectedShapesFromRectangle(Rectangle selection);
    void setActiveShape();
    void setActivePrimaryColor();
    void setActivePrimaryColor(ShapeColor color);
    void setActiveSecondaryColor();
    void setActiveShadingType();
    void setActiveStartAndEndPointMode();
    ArrayList<JPaintShapeAdapter> getShapes();
    ShapeType getActiveShapeType();
    ShapeColor getActivePrimaryColor();
    ShapeColor getActiveSecondaryColor();
    ShapeShadingType getActiveShapeShadingType();
    StartAndEndPointMode getActiveStartAndEndPointMode();
    ArrayList<JPaintShapeAdapter> getSelectedShapes();
    void showConsole();
}
