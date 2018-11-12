package model;

import java.awt.Point;
import java.lang.Math;

public class Geometry
{
    public static Dimensions getDimensionsWithInvert(Point startXY, Point endXY) 
    {
        int x = startXY.x;
        int y = startXY.y;
        int width = endXY.x - x;
        int height = endXY.y - y;
        if (width < 0)
        {
            x = endXY.x;
            width = Math.abs(width);
        }
        if (height < 0)
        {
            y = endXY.y;
            height = Math.abs(height);
        }
        return new Dimensions(new Point(x, y), new Point(width, height));
    }
}
