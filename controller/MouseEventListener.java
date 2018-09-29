package controller;

import view.gui.PaintCanvas;
import model.*;
import model.interfaces.IApplicationState;

import view.interfaces.IUiModule;

import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import java.lang.Math;

public class MouseEventListener extends MouseInputAdapter implements IMouseEventListener {
    private int mouseX;
    private int mouseY;
    private int moveX;
    private int moveY;
    private final IApplicationState applicationState;
    private final IUiModule uiModule;
    private StateModelAdapter clickedShape;
    private StateModelAdapter draggedShape;

    public MouseEventListener(IUiModule uiModule, IApplicationState applicationState) 
    { 
        this.uiModule = uiModule;
        this.applicationState = applicationState;
        this.mouseX = 0;
        this.mouseY = 0;
        this.moveX = 0;
        this.moveY = 0;
    } 

    @Override
    public void mousePressed(MouseEvent evt) {
        int startX = evt.getX();
        int startY = evt.getY();
        mouseX = startX;
        mouseY = startY;
        moveX = startX;
        moveY = startY;
        StartAndEndPointMode mode = applicationState.getActiveStartAndEndPointMode();
        PaintCanvas canvas = uiModule.getCanvas();
        switch (mode)
        {
            case DRAW:
                break;
            case MOVE:
            {
                clickedShape = canvas.getShapeFromBuffer(evt.getPoint());
                if (clickedShape != null)
                {
                    System.out.println("Found shape from buffer: " + clickedShape.shape + " " + clickedShape.x + " " + clickedShape.y + " " + clickedShape.width + " " + clickedShape.height);
                }
                break;
            }
            case SELECT:
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        int endX = evt.getX();
        int endY = evt.getY();
        StartAndEndPointMode mode = applicationState.getActiveStartAndEndPointMode();
        switch (mode) 
        {
            case DRAW:  
            {
                PaintCanvas canvas  = uiModule.getCanvas();
                StateModelAdapter adapter = new StateModelAdapter(applicationState.getActiveShapeType(),
                                                                  applicationState.getActivePrimaryColor(),
                                                                  applicationState.getActiveSecondaryColor(),
                                                                  applicationState.getActiveShapeShadingType(),
                                                                  mode
                );
                int x = mouseX;
                int y = mouseY;
                int width = endX - mouseX;
                int height = endY - mouseY;
                if (width < 0)
                {
                    x = endX;
                    width = Math.abs(width);
                }
                if (height < 0)
                {
                    y = endY;
                    height = Math.abs(height);
                }
                adapter.setShape(x, y, width, height);
                canvas.setTempShape(adapter);
                canvas.repaint();
                break;
            }
            case MOVE:
            {
                if (clickedShape != null)
                {
                    PaintCanvas canvas  = uiModule.getCanvas();
                    StateModelAdapter adapter = new StateModelAdapter(clickedShape.shapeType,
                                                                      clickedShape.primaryShapeColor,
                                                                      clickedShape.secondaryShapeColor,
                                                                      clickedShape.shapeShadingType,
                                                                      mode
                    );
                    adapter.setShape(clickedShape.x - (mouseX-moveX), 
                                     clickedShape.y - (mouseY-moveY), 
                                     clickedShape.width, 
                                     clickedShape.height
                    );
                    draggedShape = adapter;
                    canvas.setTempShape(adapter);
                    canvas.repaint();
                }
                break;
            }
            case SELECT:
                break;
        }
        moveX = endX;
        moveY = endY;
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        int endX = evt.getX();
        int endY = evt.getY();

        PaintCanvas canvas  = uiModule.getCanvas();
        StartAndEndPointMode mode = applicationState.getActiveStartAndEndPointMode();
        switch (mode)
        {
            case DRAW:
            {
                StateModelAdapter adapter = new StateModelAdapter(applicationState.getActiveShapeType(),
                                                                  applicationState.getActivePrimaryColor(),
                                                                  applicationState.getActiveSecondaryColor(),
                                                                  applicationState.getActiveShapeShadingType(),
                                                                  mode
                );
                int x = mouseX;
                int y = mouseY;
                int width = endX - mouseX;
                int height = endY - mouseY;
                if (width < 0)
                {
                    x = endX;
                    width = Math.abs(width);
                }
                if (height < 0)
                {
                    y = endY;
                    height = Math.abs(height);
                }
                adapter.setShape(x, y, width, height);
                adapter.setShape(x, y, width, height);
                canvas.addShapeAttribute(adapter);
                canvas.repaint();
                break;
            }
            case MOVE:
            {
                if (draggedShape != null)
                {
                    canvas.addShapeAttribute(draggedShape);
                    canvas.removeShapeFromBuffer(clickedShape);
                }
                canvas.repaint();
                break;
            }
            case SELECT:
                break;
        }
        mouseX = endX;
        mouseY = endY;
    }
}

