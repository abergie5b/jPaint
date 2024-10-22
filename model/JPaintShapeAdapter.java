package model;

import java.awt.*;
import java.awt.geom.*;
import model.interfaces.IShapeAdapter;

public class JPaintShapeAdapter implements IShapeAdapter {
    private int x;
    private int y;
    private int width;
    private int height;
    private Shape shape;
    private JPaintShape jpaintShape;

    public JPaintShapeAdapter(JPaintShape shape, Dimensions dims)
    {
        this.x = dims.xy.x;
        this.y = dims.xy.y;
        this.width = dims.wh.x;
        this.height = dims.wh.y;
        this.jpaintShape = shape;
        this.shape = this.getShapeWithDimensions();
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Shape getShape() {
        return this.shape;
    }

    @Override
    public JPaintShape getJPaintShape() {
        return this.jpaintShape;
    }

    private Shape getShapeWithDimensions()
    {
        Shape _shape;
        switch (this.jpaintShape.getShapeType())
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
                throw new IllegalArgumentException("Invalid ShapeType found converting JPaintShape");
            }
        }
        return _shape;
    }

}

