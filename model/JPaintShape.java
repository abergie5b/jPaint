package model;

import java.awt.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class JPaintShape {
    private ShapeType shapeType;
    private ShapeColor primaryShapeColor;
    private Color primaryColor;
    private ShapeColor secondaryShapeColor;
    private Color secondaryColor;
    private ShapeShadingType shapeShadingType;
    private StartAndEndPointMode startAndEndPointMode;

    public JPaintShape(ShapeType shapeType,
                       ShapeColor primaryColor,
                       ShapeColor secondaryColor,
                       ShapeShadingType shapeShadingType,
                       StartAndEndPointMode startAndEndPointMode)
    {
        this.shapeType = shapeType;
        this.primaryShapeColor = primaryColor;
        this.primaryColor = this.colorToObject(primaryColor);
        this.secondaryShapeColor = secondaryColor;
        this.secondaryColor = this.colorToObject(secondaryColor);
        this.shapeShadingType = shapeShadingType;
        this.startAndEndPointMode = startAndEndPointMode;
    }


    public ShapeType getShapeType() {
        return this.shapeType;
    }

    public ShapeColor getPrimaryShapeColor() {
        return this.primaryShapeColor;
    }

    public Color getPrimaryColor() {
        return this.primaryColor;
    }

    public Color getSecondaryColor() {
        return this.secondaryColor;
    }

    public ShapeColor getSecondaryShapeColor() {
        return secondaryShapeColor;
    }

    public ShapeShadingType getShapeShadingType() {
        return this.shapeShadingType;
    }

    public StartAndEndPointMode getStartAndEndPointMode() {
        return this.startAndEndPointMode;
    }

    public void setShapeShadingType(ShapeShadingType shadingType) {
        this.shapeShadingType = shadingType;
    }

    public void setStartAndEndPointMode(StartAndEndPointMode mode) {
        this.startAndEndPointMode = mode;
    }

    public String getShapeName() {
        return this.shapeType.name();
    }

    public String getPrimaryColorName() {
        return this.primaryShapeColor.name();
    }

    public String getSecondaryColorName() {
        return this.secondaryShapeColor.name();
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public void setPrimaryColor(ShapeColor color) {
        this.primaryShapeColor = color;
        this.primaryColor = this.colorToObject(color);
    }

    public void setSecondaryColor(ShapeColor color) {
        this.secondaryShapeColor = color;
        this.secondaryColor = this.colorToObject(color);
    }

    public void updateShadingType(ShapeShadingType shading) {
        this.shapeShadingType = shading;
    }

    private Color colorToObject(ShapeColor color) {
        Field[] fields = Color.class.getDeclaredFields();
        for (Field f: fields) 
        {
            if (color.name().equals(f.getName()) && Modifier.isStatic(f.getModifiers()))
            {
                try
                {
                    return (Color)Color.class.getDeclaredField(color.name()).get(null);
                }
                catch (NoSuchFieldException e) {
                    System.out.println("Invalid Color found trying to convert from string\n" + e);
                }
                catch (IllegalAccessException e) {
                    System.out.println("Unable to get Color attribute trying to convert from string\n" + e);
                }
            }
        }
        // #TODO throw error
        return Color.BLACK;
    }
}
