package model.interfaces;

import model.*;
import java.awt.Color;
import java.awt.Shape;

public interface IShapeAdapter
{
    int getWidth();
    String getShapeName();
    String getPrimaryColorName();
    String getSecondaryColorName();
    void setShapeType(ShapeType shapeType);
    void updatePrimaryColor(ShapeColor color);
    void updateSecondaryColor(ShapeColor color);
    void setShape(Dimensions dims);
    ShapeAdapter convert(Dimensions dims);
    Shape shapeToObject(Dimensions dims);
    Color colorToObject(ShapeColor color);
}
