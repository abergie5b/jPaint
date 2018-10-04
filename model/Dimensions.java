package model;

import java.awt.Point;
import javax.swing.*;

public class Dimensions
{
    public Point xy;
    public Point wh;

    public Dimensions(Point xy, Point wh)
    {
        this.xy = xy;
        this.wh = wh;
    }

    public void setSize(Point wh)
    {
        this.wh = wh;
    }

    public void move(Point xy)
    {
        this.xy = xy;
    }
}