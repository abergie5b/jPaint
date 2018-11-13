package model.interfaces;

import model.*;
import java.awt.Shape;

public interface IShapeAdapter
{
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    JPaintShape getJPaintShape();
    Shape getShape();
}
