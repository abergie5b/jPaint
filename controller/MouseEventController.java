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
    private final IApplicationState appState;
    private Point mousePoint;
    private Point mouseDragPoint;
    
    public MouseEventController(IApplicationState appState) 
    { 
        this.appState = appState;
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
        this.appState.setClickedShape(e.getPoint());
    }

    @Override
    public void mouseDraggedDraw(MouseEvent e) {
        this.setMouseDraggedPosition(e.getPoint());
        ShapeColor primaryColor = this.appState.getActivePrimaryColor();
        ShapeColor secondaryColor = this.appState.getActiveSecondaryColor();
        if (SwingUtilities.isRightMouseButton(e))
        {
            secondaryColor = primaryColor;
            primaryColor = this.appState.getActiveSecondaryColor();
        }
        ShapeAdapter adapter = new ShapeAdapter(this.appState.getActiveShapeType(),
                                                primaryColor,
                                                secondaryColor,
                                                this.appState.getActiveShapeShadingType(),
                                                StartAndEndPointMode.DRAW
        );
        ShapeAdapter shape = adapter.convert(Geometry.getDimensionsWithInvert(this.mousePoint, this.mouseDragPoint));
        this.appState.setMouseDraggedShape(shape);
        this.appState.setDraggedShape(shape);
        this.appState.repaint();
    }

    @Override
    public void mouseReleasedDraw(MouseEvent e) {
        if (this.appState.getDraggedShape() != null)
        {
            this.appState.addShapeAttribute(this.appState.getDraggedShape());
        }
        this.setMousePosition(e.getPoint());
        this.appState.resetDraggedShape();
        this.appState.repaint();
    }

    @Override
    public void mousePressedMove(MouseEvent e) {
        Point point = e.getPoint();
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(e.getPoint());
        this.appState.setClickedShape(point);
    }

    @Override
    public void mouseDraggedMove(MouseEvent e) {
        if (this.appState.getClickedShape() != null)
        {
            ShapeAdapter adapter = new ShapeAdapter(this.appState.getClickedShape().shapeType,
                                                    this.appState.getClickedShape().primaryShapeColor,
                                                    this.appState.getClickedShape().secondaryShapeColor,
                                                    this.appState.getClickedShape().shapeShadingType,
                                                    StartAndEndPointMode.MOVE);
            int deltaX = this.mousePoint.x - this.mouseDragPoint.x;
            int deltaY = this.mousePoint.y - this.mouseDragPoint.y;
            ShapeAdapter shape = adapter.convert(new Dimensions(new Point(this.appState.getClickedShape().x - deltaX, 
                                                                          this.appState.getClickedShape().y - deltaY),
                                                                new Point(this.appState.getClickedShape().width, 
                                                                          this.appState.getClickedShape().height)
                                            )
            );
            this.appState.setDraggedShape(shape);
            this.appState.setMouseDraggedShape(shape);
            this.appState.repaint();
        }
        this.setMouseDraggedPosition(e.getPoint());
    }

    @Override
    public void mouseReleasedMove(MouseEvent e) {
        if (this.appState.getDraggedShape() != null && this.appState.getClickedShape() != null)
        {
            this.appState.removeShapeFromBuffer(this.appState.getClickedShape());
            this.appState.addShapeAttribute(this.appState.getDraggedShape());
        }
        this.setMousePosition(e.getPoint());
        this.appState.resetDraggedShape();
        this.appState.repaint();
    }

    @Override
    public void mousePressedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(e.getPoint());
        this.appState.setClickedShape(e.getPoint());
        this.appState.repaint();
    }

    @Override
    public void mouseDraggedSelect(MouseEvent e) {
        Dimensions dims = Geometry.getDimensionsWithInvert(this.mousePoint, this.mouseDragPoint);
        Rectangle selectionRectangle = new Rectangle(dims.xy.x, dims.xy.y, dims.wh.x, dims.wh.y);
        this.appState.setSelectedShapes(selectionRectangle);
        ShapeAdapter adapter = new ShapeAdapter(ShapeType.RECTANGLE,
                                                ShapeColor.BLACK,
                                                ShapeColor.BLACK,
                                                ShapeShadingType.OUTLINE,
                                                StartAndEndPointMode.SELECT);
        ShapeAdapter shape = adapter.convert(dims);
        this.setMouseDraggedPosition(e.getPoint());
        this.appState.setMouseDraggedShape(shape);
        this.appState.repaint();
    }

    @Override
    public void mouseReleasedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.appState.repaint();
    }
}

