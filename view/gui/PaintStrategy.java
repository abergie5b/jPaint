package view.gui;

import model.*;
import view.gui.MouseEventListener;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class FilledInPaintStrategy extends IPaintStrategy
{
    private Graphics2D g2d;
    private ShapeAdapter shape;

    public FilledInPaintStrategy(Graphics2D g2d, ShapeAdapter shape)
    {
        this.g2d = g2d;
        this.shape = shape;
    }

    void execute()
    {
        g2d.setPaint(s.primaryColor);
        g2d.fill(s.shape);
    }


}
