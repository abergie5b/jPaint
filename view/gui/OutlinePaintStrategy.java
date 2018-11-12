package view.gui;

import model.*;

import java.awt.*;

public class OutlinePaintStrategy implements IPaintStrategy
{
    private Graphics2D g2d;
    private JPaintShapeAdapter shape;

    public OutlinePaintStrategy(Graphics2D g2d, JPaintShapeAdapter shape)
    {
        this.g2d = g2d;
        this.shape = shape;
    }

    public void execute()
    {
        g2d.setPaint(shape.getJPaintShape().getPrimaryColor());
        g2d.draw(shape.getShape());
    }
}
