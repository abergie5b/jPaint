package controller;

import model.*;
import model.interfaces.IApplicationState;

import java.awt.*;
import javax.swing.*;
import java.lang.Math;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;

public class MouseEventController implements IMouseEventController
{
    private final IApplicationState applicationState;
    private Point mousePoint;
    private Point mouseDragPoint;
    
    public MouseEventController(IApplicationState applicationState) 
    { 
        this.applicationState = applicationState;
        this.mousePoint = new Point(0, 0);
        this.mouseDragPoint = new Point(0, 0);
    } 

    public void printEvent(String event) {
        System.out.println("MouseEvent(" + event + "):" + " mouseX " + mousePoint.x +  " mouseY: " + mousePoint.y + " mouseDragX: " + mouseDragPoint.x + " mouseDragY: " + mouseDragPoint.y);
    }

    private void setMousePosition(Point point)
    {
        this.mousePoint = point;
    }

    private void setMouseDraggedPosition(Point point)
    {
        this.mouseDragPoint = point;
    }

    @Override
    public void mousePressedDraw(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(e.getPoint());
        this.applicationState.setClickedShape(e.getPoint());
    }

    @Override
    public void mouseDraggedDraw(MouseEvent e) {
        ShapeColor primaryColor = this.applicationState.getActivePrimaryColor();
        ShapeColor secondaryColor = this.applicationState.getActiveSecondaryColor();
        if (SwingUtilities.isRightMouseButton(e))
        {
            secondaryColor = primaryColor;
            primaryColor = this.applicationState.getActiveSecondaryColor();
        }
        ShapeAdapter adapter = new ShapeAdapter(this.applicationState.getActiveShapeType(),
                                                primaryColor,
                                                secondaryColor,
                                                this.applicationState.getActiveShapeShadingType(),
                                                StartAndEndPointMode.DRAW
        );
        this.setMouseDraggedPosition(e.getPoint());
        adapter.setShape(Geometry.getDimensionsWithInvert(this.mousePoint, this.mouseDragPoint));
        this.applicationState.setTempShape(adapter);
        this.applicationState.setDraggedShape(adapter);
        this.applicationState.repaint();
    }

    @Override
    public void mouseReleasedDraw(MouseEvent e) {
        if (this.applicationState.getDraggedShape() != null)
        {
            this.applicationState.addShapeAttribute(this.applicationState.getDraggedShape());
        }
        this.setMousePosition(e.getPoint());
        
        this.applicationState.resetDraggedShape();
        this.applicationState.repaint();
    }

    @Override
    public void mousePressedMove(MouseEvent e) {
        Point point = e.getPoint();
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(e.getPoint());
        
        this.applicationState.setClickedShape(point);
    }

    @Override
    public void mouseDraggedMove(MouseEvent e) {
        if (this.applicationState.getClickedShape() != null)
        {
            ShapeAdapter adapter = new ShapeAdapter(this.applicationState.getClickedShape().shapeType,
                                                    this.applicationState.getClickedShape().primaryShapeColor,
                                                    this.applicationState.getClickedShape().secondaryShapeColor,
                                                    this.applicationState.getClickedShape().shapeShadingType,
                                                    StartAndEndPointMode.MOVE);
            int deltaX = this.mousePoint.x - this.mouseDragPoint.x;
            int deltaY = this.mousePoint.y - this.mouseDragPoint.y;
            adapter.setShape(new Dimensions(new Point(this.applicationState.getClickedShape().x - deltaX, this.applicationState.getClickedShape().y - deltaY),
                                            new Point(this.applicationState.getClickedShape().width, this.applicationState.getClickedShape().height))
            );
            this.applicationState.setDraggedShape(adapter);
            this.applicationState.setTempShape(adapter);
            this.applicationState.repaint();
        }
        this.setMouseDraggedPosition(e.getPoint());
    }

    @Override
    public void mouseReleasedMove(MouseEvent e) {
        if (this.applicationState.getDraggedShape() != null && this.applicationState.getClickedShape() != null)
        {
            this.applicationState.removeShapeFromBuffer(this.applicationState.getClickedShape());
            this.applicationState.addShapeAttribute(this.applicationState.getDraggedShape());
        }
        this.setMousePosition(e.getPoint());
        
        this.applicationState.resetDraggedShape();
        this.applicationState.repaint();
    }

    @Override
    public void mousePressedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(e.getPoint());
        
        this.applicationState.setClickedShape(e.getPoint());
        this.applicationState.repaint();
    }

    @Override
    public void mouseDraggedSelect(MouseEvent e) {
        Dimensions dims = Geometry.getDimensionsWithInvert(this.mousePoint, this.mouseDragPoint);
        Rectangle selectionRectangle = new Rectangle(dims.xy.x, dims.xy.y, dims.wh.x, dims.wh.y);
        this.applicationState.setSelectedShapes(this.applicationState.getShapesinSelection(selectionRectangle));
        ShapeAdapter adapter = new ShapeAdapter(ShapeType.RECTANGLE,
                                                ShapeColor.BLACK,
                                                ShapeColor.BLACK,
                                                ShapeShadingType.OUTLINE,
                                                StartAndEndPointMode.SELECT);
        adapter.setShape(dims);
        this.setMouseDraggedPosition(e.getPoint());
        this.applicationState.setTempShape(adapter);
        this.applicationState.repaint();
    }

    @Override
    public void mouseReleasedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.applicationState.repaint();
    }
}

