package model.interfaces;

import model.*;
import java.util.ArrayList;
import java.awt.*;

public interface IApplicationState 
{
    void repaint();
    JPaintShapeAdapter getDraggedShape();
    JPaintShapeAdapter getClickedShape();
    void addShapeAttribute(JPaintShapeAdapter _shape);
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
    void setActiveSecondaryColor();
    void setActiveShadingType();
    void setActiveStartAndEndPointMode();
    void resetDraggedShape();
    ArrayList<JPaintShapeAdapter> getShapes();
    ShapeType getActiveShapeType();
    ShapeColor getActivePrimaryColor();
    ShapeColor getActiveSecondaryColor();
    ShapeShadingType getActiveShapeShadingType();
    StartAndEndPointMode getActiveStartAndEndPointMode();
    ArrayList<JPaintShapeAdapter> getSelectedShapes();
    JPaintShapeAdapter getMouseDraggedMoveShape(Point mousePoint, Point mouseDragPoint);
    JPaintShapeAdapter getMouseDraggedDrawShape(Point mousePoint, Point mouseDragPoint, boolean flippedColors);
}
