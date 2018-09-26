package model;

import java.awt.*;
import java.awt.geom.*;

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
        if (primaryColor == ShapeColor.BLUE)
        {
            return Color.BLUE;
        }
        else if (primaryColor == ShapeColor.GREEN)
        {
            return Color.GREEN;
        }
        return Color.BLACK;
    }

    public Color secondaryColorToObject() {
        if (secondaryColor == ShapeColor.BLUE)
        {
            return Color.BLUE;
        }
        else if (secondaryColor == ShapeColor.GREEN)
        {
            return Color.GREEN;
        }
        return Color.BLACK;
    }

}
