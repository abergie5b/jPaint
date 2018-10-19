package model.interfaces;

import model.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;

public interface IApplicationState 
{
    ShapeAdapter getDraggedShape();
    ShapeAdapter getClickedShape();
    ArrayList<ShapeAdapter> getSelectedShapes();
    void setDraggedShape(ShapeAdapter shape);
    void resetDraggedShape();
    void setClickedShape(Point point);
    void repaint();
    ArrayList<ShapeAdapter> getShapesinSelection(Rectangle selection);
    void removeShapeFromBuffer(ShapeAdapter shape);
    void addShapeAttribute(ShapeAdapter _shape);
    ShapeAdapter getShapeFromBuffer(Point point);
    void undo();
    void redo();
    void delete();
    void copy();
    void paste();
    void setCanvasShapes();
    void setSelectedShapes(Rectangle selection);
    void setActiveShape();
    void setActivePrimaryColor();
    void setActiveSecondaryColor();
    void setActiveShadingType();
    void setActiveStartAndEndPointMode();
    void setMouseDraggedShape(ShapeAdapter shape);
    ShapeType getActiveShapeType();
    ShapeColor getActivePrimaryColor();
    ShapeColor getActiveSecondaryColor();
    ShapeShadingType getActiveShapeShadingType();
    StartAndEndPointMode getActiveStartAndEndPointMode();
}
