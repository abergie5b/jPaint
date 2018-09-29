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

public class MouseEventController implements IMouseEventController
{
    private int mouseX;
    private int mouseY;
    private int moveX;
    private int moveY;
    private StateModelAdapter clickedShape;
    private StateModelAdapter draggedShape;
    private final IApplicationState applicationState;
    private final IUiModule uiModule;
    private final PaintCanvas canvas;

    public MouseEventController(IUiModule uiModule, IApplicationState applicationState) 
    { 
        this.uiModule = uiModule;
        this.applicationState = applicationState;
        this.canvas = uiModule.getCanvas();
        this.mouseX = 0;
        this.mouseY = 0;
        this.moveX = 0;
        this.moveY = 0;
    } 

    public void mousePressedDraw(int startX, int startY) {
        mouseX = startX;
        mouseY = startY;
        moveX = startX;
        moveY = startY;
    }

    public void mousePressedMove(Point point) {
        int startX = point.x;
        int startY = point.y;
        mouseX = startX;
        mouseY = startY;
        moveX = startX;
        moveY = startY;
        clickedShape = canvas.getShapeFromBuffer(point);
        if (clickedShape != null)
        {
            System.out.println("Found shape from buffer: " + clickedShape.shape + " " + clickedShape.x + " " + clickedShape.y + " " + clickedShape.width + " " + clickedShape.height);
        }
    }

    @Override
    public void mousePressedSelect(int startX, int startY) {
        mouseX = startX;
        mouseY = startY;
        moveX = startX;
        moveY = startY;
    }

    @Override
    public void mouseDraggedDraw(int endX, int endY) {
        StateModelAdapter adapter = new StateModelAdapter(applicationState.getActiveShapeType(),
                                                          applicationState.getActivePrimaryColor(),
                                                          applicationState.getActiveSecondaryColor(),
                                                          applicationState.getActiveShapeShadingType(),
                                                          StartAndEndPointMode.DRAW
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
        moveX = endX;
        moveY = endY;
    }

    @Override
    public void mouseDraggedMove(int endX, int endY) {
        if (clickedShape != null)
        {
            StateModelAdapter adapter = new StateModelAdapter(clickedShape.shapeType,
                                                              clickedShape.primaryShapeColor,
                                                              clickedShape.secondaryShapeColor,
                                                              clickedShape.shapeShadingType,
                                                              StartAndEndPointMode.MOVE);
            adapter.setShape(clickedShape.x - (mouseX-moveX), 
                             clickedShape.y - (mouseY-moveY), 
                             clickedShape.width, 
                             clickedShape.height);
            draggedShape = adapter;
            canvas.setTempShape(adapter);
            canvas.repaint();
        }
        moveX = endX;
        moveY = endY;
    }

    @Override
    public void mouseDraggedSelect(int endX, int endY) {
        moveX = endX;
        moveY = endY;
    }

    @Override
    public void mouseReleasedDraw(int endX, int endY) {
        StateModelAdapter adapter = new StateModelAdapter(applicationState.getActiveShapeType(),
                                                          applicationState.getActivePrimaryColor(),
                                                          applicationState.getActiveSecondaryColor(),
                                                          applicationState.getActiveShapeShadingType(),
                                                          StartAndEndPointMode.DRAW
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
        mouseX = endX;
        mouseY = endY;
    }

    @Override
    public void mouseReleasedMove(int endX, int endY) {
        if (draggedShape != null)
        {
            canvas.removeShapeFromBuffer(clickedShape);
            canvas.addShapeAttribute(draggedShape);
        }
        canvas.repaint();
        mouseX = endX;
        mouseY = endY;
    }

    @Override
    public void mouseReleasedSelect(int endX, int endY) {
        mouseX = endX;
        mouseY = endY;
    }
}

