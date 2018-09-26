package model;

import java.awt.*;
import java.awt.geom.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class StateModel {

    public ShapeType shapeType;
    public ShapeColor primaryColor;
    public ShapeColor secondaryColor;
    public ShapeShadingType shapeShadingType;
    public StartAndEndPointMode startAndEndPointMode;

    public StateModel(ShapeType shapeType, 
                      ShapeColor primaryColor, 
                      ShapeColor secondaryColor, 
                      ShapeShadingType shapeShadingType, 
                      StartAndEndPointMode startAndEndPointMode) {
        this.shapeType = shapeType;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.shapeShadingType = shapeShadingType;
        this.startAndEndPointMode = startAndEndPointMode;
    }

    public Shape shapeToObject(int mouseX, int mouseY, int endX, int endY) {
        if (shapeType == ShapeType.RECTANGLE)
        {
            return new Rectangle(mouseX, mouseY, endX - mouseX, endY - mouseY);
        }
        else if (shapeType == ShapeType.ELLIPSE)
        {
            return new Ellipse2D.Double(mouseX, mouseY, endX - mouseX, endY - mouseY);
        }
        else if (shapeType == ShapeType.TRIANGLE)
        {
            //return new Triangle(mouseX, mouseY, endX - mouseX, endY - mouseY);
        }
        return new Rectangle(mouseX, mouseY, endX - mouseX, endY - mouseY);
    }

    public Color primaryColorToObject() {
        Field[] fields = Color.class.getDeclaredFields();
        for (Field f: fields) 
        {
            if (primaryColor.name() == f.getName() && Modifier.isStatic(f.getModifiers()))
            {
                try
                {
                    return (Color)Color.class.getDeclaredField(primaryColor.name()).get(null);
                }
                catch (NoSuchFieldException e)
                {
                }
                catch (IllegalAccessException e)
                {
                }
            }
        }
        // #TODO throw error
        return Color.BLACK;
    }

    public Color secondaryColorToObject() {
        Field[] fields = Color.class.getDeclaredFields();
        for (Field f: fields) 
        {
            if (secondaryColor.name() == f.getName() && Modifier.isStatic(f.getModifiers()))
            {
                try
                {
                    return (Color)Color.class.getDeclaredField(secondaryColor.name()).get(null);
                }
                catch (NoSuchFieldException e)
                {
                }
                catch (IllegalAccessException e)
                {
                }
            }
        }
        // #TODO throw error
        return Color.BLACK;
    }

}
