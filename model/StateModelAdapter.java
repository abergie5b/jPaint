package model;

import java.awt.*;
import java.awt.geom.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class StateModelAdapter {

    public ShapeType shapeType;
    public Color primaryColor;
    public Color secondaryColor;
    public ShapeShadingType shapeShadingType;
    public StartAndEndPointMode startAndEndPointMode;

    public StateModelAdapter(ShapeType shapeType, ShapeColor primaryColor, ShapeColor secondaryColor, ShapeShadingType shapeShadingType, StartAndEndPointMode startAndEndPointMode) 
    {
        this.shapeType = shapeType;
        this.primaryColor = this.colorToObject(primaryColor);
        this.secondaryColor = this.colorToObject(secondaryColor);
        this.shapeShadingType = shapeShadingType;
        this.startAndEndPointMode = startAndEndPointMode;
    }

    public String getShapeName() {
        String name = this.shapeType.name();
        return name;
    }

    public String getPrimaryColorName() {
        String name = this.primaryColor.getClass().getName();
        return name;
    }

    public String getSecondaryColorName() {
        String name = this.secondaryColor.getClass().getName();
        return name;
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public void updatePrimaryColor(ShapeColor color) {
        this.primaryColor = this.colorToObject(color);
    }

    public void updateSecondaryColor(ShapeColor color) {
        this.secondaryColor = this.colorToObject(color);
    }

    public Shape createShape(int mouseX, int mouseY, int endX, int endY) {
        Shape shape = shapeToObject(mouseX, mouseY, endX, endY);
        return shape;
    }

    private Shape shapeToObject(int mouseX, int mouseY, int endX, int endY) {
        Shape shape = null;
        switch (shapeType) {
            case RECTANGLE:
                shape = new Rectangle(mouseX, mouseY, endX - mouseX, endY - mouseY);
                break;
            case ELLIPSE:
                shape = new Ellipse2D.Double(mouseX, mouseY, endX - mouseX, endY - mouseY);
                break;
            case TRIANGLE:
                //return new Triangle(mouseX, mouseY, endX - mouseX, endY - mouseY);
                break;
            default:
                shape = new Rectangle(mouseX, mouseY, endX - mouseX, endY - mouseY);
                break;
        }
        return shape;
    }

    private Color colorToObject(ShapeColor color) {
        Field[] fields = Color.class.getDeclaredFields();
        for (Field f: fields) 
        {
            if (color.name() == f.getName() && Modifier.isStatic(f.getModifiers()))
            {
                try
                {
                    return (Color)Color.class.getDeclaredField(color.name()).get(null);
                }
                catch (NoSuchFieldException e) { }
                catch (IllegalAccessException e) { }
            }
        }
        // #TODO throw error
        return Color.BLACK;
    }

}
