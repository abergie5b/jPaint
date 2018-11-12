package view.gui;

import model.*;

import java.awt.*;

public class OutlineAndFilledInPaintStrategy implements IPaintStrategy
{
    private Graphics2D g2d;
    private JPaintShapeAdapter shape;

    public OutlineAndFilledInPaintStrategy(Graphics2D g2d, JPaintShapeAdapter shape)
    {
        this.g2d = g2d;
        this.shape = shape;
    }

    public void execute()
    {
        g2d.setPaint(shape.getJPaintShape().getPrimaryColor());
        g2d.fill(shape.getShape());
        g2d.setPaint(shape.getJPaintShape().getSecondaryColor());
        g2d.draw(shape.getShape());
    }
}
