package controller;

import model.*;
import model.interfaces.IApplicationState;
import view.interfaces.IUiModule;
import view.gui.PaintCanvas;

import java.lang.Math;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class MouseEventController implements IMouseEventController
{
    private int mouseX;
    private int mouseY;
    private int moveX;
    private int moveY;
    private StateModelAdapter clickedShape;
    private StateModelAdapter draggedShape;
    private ArrayList<StateModelAdapter> selectedShapes;
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

    public void printEvent(String event) {
        System.out.println("MouseEvent(" + event + "):" + " mouseX " + mouseX +  " mouseY: " + mouseY + " moveX: " + moveX + " moveY: " + moveY);
    }

    @Override
    public void mousePressedDraw(MouseEvent e) {
        int startX = e.getX();
        int startY = e.getY();
        mouseX = startX;
        mouseY = startY;
        clickedShape = canvas.getShapeFromBuffer(e.getPoint());
    }

    @Override
    public void mouseDraggedDraw(MouseEvent e) {
        int endX = e.getX();
        int endY = e.getY();
        ShapeColor primaryColor = applicationState.getActivePrimaryColor();
        ShapeColor secondaryColor = applicationState.getActiveSecondaryColor();
        if (SwingUtilities.isRightMouseButton(e))
        {
            secondaryColor = primaryColor;
            primaryColor = applicationState.getActiveSecondaryColor();
        }
        StateModelAdapter adapter = new StateModelAdapter(applicationState.getActiveShapeType(),
                                                          primaryColor,
                                                          secondaryColor,
                                                          applicationState.getActiveShapeShadingType(),
                                                          StartAndEndPointMode.DRAW
        );
        int x = mouseX;
        int y = mouseY;
        int width = endX - x;
        int height = endY - y;
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
        this.draggedShape = adapter;
        canvas.repaint();
    }

    @Override
    public void mouseReleasedDraw(MouseEvent e) {
        int endX = e.getX();
        int endY = e.getY();
        canvas.addShapeAttribute(this.draggedShape);
        canvas.repaint();
        mouseX = endX;
        mouseY = endY;
        this.draggedShape = null;
    }

    @Override
    public void mousePressedMove(MouseEvent e) {
        Point point = e.getPoint();
        mouseX = point.x;
        mouseY = point.y;
        moveX = mouseX;
        moveY = mouseY;
        clickedShape = canvas.getShapeFromBuffer(point);
        if (clickedShape != null)
        {
            System.out.println("Found shape from buffer: " + clickedShape.shape + " " + clickedShape.x + " " + clickedShape.y + " " + clickedShape.width + " " + clickedShape.height);
        }
    }

    @Override
    public void mouseDraggedMove(MouseEvent e) {
        int endX = e.getX();
        int endY = e.getY();
        if (clickedShape != null)
        {
            StateModelAdapter adapter = new StateModelAdapter(clickedShape.shapeType,
                                                              clickedShape.primaryShapeColor,
                                                              clickedShape.secondaryShapeColor,
                                                              clickedShape.shapeShadingType,
                                                              StartAndEndPointMode.MOVE);
            int deltaX = mouseX - moveX;
            int deltaY = mouseY - moveY;
            adapter.setShape(clickedShape.x - deltaX,
                             clickedShape.y - deltaY,
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
    public void mouseReleasedMove(MouseEvent e) {
        int endX = e.getX();
        int endY = e.getY();
        if (draggedShape != null)
        {
            canvas.removeShapeFromBuffer(clickedShape);
            canvas.addShapeAttribute(draggedShape);
        }
        canvas.repaint();
        mouseX = endX;
        mouseY = endY;
        draggedShape = null;
    }


    @Override
    public void mousePressedSelect(MouseEvent e) {
        int startX = e.getX();
        int startY = e.getY();
        mouseX = startX;
        mouseY = startY;
        moveX = startX;
        moveY = startY;
        clickedShape = canvas.getShapeFromBuffer(e.getPoint());
        if (clickedShape != null)
        {
            System.out.println("Found shape from buffer: " + clickedShape.shape + " " + clickedShape.x + " " + clickedShape.y + " " + clickedShape.width + " " + clickedShape.height);
        }
        canvas.repaint();
    }

    @Override
    public void mouseDraggedSelect(MouseEvent e) {
        int endX = e.getX();
        int endY = e.getY();
        int x = mouseX;
        int y = mouseY;
        int width = endX - x;
        int height = endY - y;
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
        Rectangle selectionRectangle = new Rectangle(x, y, width, height);
        selectedShapes = canvas.getShapesinSelection(selectionRectangle);
        StateModelAdapter adapter = new StateModelAdapter(ShapeType.RECTANGLE,
                                                          ShapeColor.BLACK,
                                                          ShapeColor.BLACK,
                                                          ShapeShadingType.OUTLINE,
                                                          StartAndEndPointMode.SELECT);
        adapter.setShape(x, y, width, height);
        canvas.setTempShape(adapter);
        canvas.repaint();
        moveX = endX;
        moveY = endY;
    }

    @Override
    public void mouseReleasedSelect(MouseEvent e) {
        int endX = e.getX();
        int endY = e.getY();
        mouseX = endX;
        mouseY = endY;
        applicationState.setSelectedShapes(selectedShapes);
        canvas.repaint();
    }
}

