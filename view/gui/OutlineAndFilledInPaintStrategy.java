package view.gui;

import model.*;
import view.gui.MouseEventListener;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class OutlineAndFilledInPaintStrategy implements IPaintStrategy
{
    private Graphics2D g2d;
    private ShapeAdapter shape;

    public OutlineAndFilledInPaintStrategy(Graphics2D g2d, ShapeAdapter shape)
    {
        this.g2d = g2d;
        this.shape = shape;
    }

    public void execute()
    {
        g2d.setPaint(shape.primaryColor);
        g2d.fill(shape.shape);
        g2d.setPaint(shape.secondaryColor);
        g2d.draw(shape.shape);
    }
}
