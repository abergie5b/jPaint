package controller;

import model.*;
import model.interfaces.IApplicationState;

import view.interfaces.IUiModule;

import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

public class MouseEventListener extends MouseInputAdapter implements IMouseEventListener {
    private int mouseX;
    private int mouseY;
    private final IApplicationState applicationState;
    private final IUiModule uiModule;

    public MouseEventListener(IUiModule uiModule, IApplicationState applicationState) 
    { 
        this.uiModule = uiModule;
        this.applicationState = applicationState;
        this.mouseX = 0;
        this.mouseY = 0;
    } 
    @Override
    public void mousePressed(MouseEvent evt) {
        int startX = evt.getX();
        int startY = evt.getY();
        mouseX = startX;
        mouseY = startY;
    }
    @Override
    public void mouseDragged(MouseEvent evt) {
        int endX = evt.getX();
        int endY = evt.getY();
        //mouseX = endX;
        //mouseY = endY;
        //repaint();
    }
    @Override
    public void mouseReleased(MouseEvent evt) {
        int endX = evt.getX();
        int endY = evt.getY();
        Shape s = null;
        if (applicationState.getActiveShapeType() == ShapeType.RECTANGLE)
        {
            s = new Rectangle(mouseX, mouseY, endX - mouseX, endY - mouseY);
        }
        else if (applicationState.getActiveShapeType() == ShapeType.ELLIPSE)
        {
            s = new Ellipse2D.Double(mouseX, mouseY, endX - mouseX, endY - mouseY);
        }
        else if (applicationState.getActiveShapeType() == ShapeType.TRIANGLE)
        {
            //s = new Triangle(mouseX, mouseY, endX - mouseX, endY - mouseY);
        }
        if (s != null)
        {
            uiModule.setCanvasShape(s);
            uiModule.getCanvas().repaint();
        }
        mouseX = endX;
        mouseY = endY;
    }
}

