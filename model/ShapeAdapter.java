package model;

import java.awt.*;
import java.awt.geom.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ShapeAdapter {
    public int x;
    public int y;
    public int width;
    public int height;
    public Shape shape;
    public ShapeType shapeType;
    public ShapeColor primaryShapeColor;
    public Color primaryColor;
    public ShapeColor secondaryShapeColor;
    public Color secondaryColor;
    public ShapeShadingType shapeShadingType;
    public StartAndEndPointMode startAndEndPointMode;

    public ShapeAdapter(ShapeType shapeType, 
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

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getShapeName() {
        String name = this.shapeType.name();
        return name;
    }

    public String getPrimaryColorName() {
        String name = this.primaryShapeColor.name();
        return name;
    }

    public String getSecondaryColorName() {
        String name = this.secondaryShapeColor.name();
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

    public void updateShadingType(ShapeShadingType shading) {
        this.shapeShadingType = shading;
    }

    public void setShape(Dimensions dims)
    {
        this.shape = shapeToObject(dims);
    }

    public ShapeAdapter convert(Dimensions dims)
    {
        // TODO return a new class of object "JShape" or something ...
        //ShapeAdapter shape =  new ShapeAdapter(this.shapeType,
        //                                       this.primaryShapeColor,
        //                                       this.secondaryShapeColor,
        //                                       this.shapeShadingType,
        //                                       this.startAndEndPointMode
        //                        );
        this.shape = shapeToObject(dims);
        return this;
    }

    private Shape shapeToObject(Dimensions dims)
    {
        this.x = dims.xy.x;
        this.y = dims.xy.y;
        this.width = dims.wh.x;
        this.height = dims.wh.y;
        Shape _shape = null;
        switch (this.shapeType) 
        {
            case ELLIPSE:
            {
                _shape = new Ellipse2D.Double(this.x, this.y, this.width, this.height);
                break;
            }
            case RECTANGLE:
            {
                _shape = new Rectangle(this.x, this.y, this.width, this.height);
                break;
            }
            case TRIANGLE:
            {
                _shape = new Polygon(new int[] {this.x+this.width/2, this.x, this.x+this.width}, 
                                     new int[] {this.y+this.height, this.y-this.height, this.y-this.height}, 
                                     3);
                break;
            }
            default:
            {
                // #TODO throw error
                _shape = new Rectangle(this.x, this.y, this.width, this.height);
                break;
            }
        }
        return _shape;
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
