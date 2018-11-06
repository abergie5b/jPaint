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

public class MouseEventCanvasController implements IMouseEventCanvasController
{
    private final IApplicationState appState;
    private Point mousePoint;
    private Point mouseDragPoint;
    
    public MouseEventCanvasController(IApplicationState appState) 
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
        this.appState.setDraggedShape(shape);
    }

    @Override
    public void mouseReleasedDraw(MouseEvent e) {
        ShapeAdapter draggedShape = this.appState.getDraggedShape();
        if (draggedShape != null)
        {
            this.appState.addShapeAttribute(draggedShape);
        }
        this.setMousePosition(e.getPoint());
        this.appState.resetDraggedShape();
    }

    @Override
    public void mousePressedMove(MouseEvent e) {
        Point point = e.getPoint();
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(point);
        this.appState.setClickedShape(point);
    }

    @Override
    public void mouseDraggedMove(MouseEvent e) {
        ShapeAdapter clickedShape = this.appState.getClickedShape();
        if (clickedShape != null)
        {
            ShapeAdapter adapter = new ShapeAdapter(clickedShape.shapeType,
                                                    clickedShape.primaryShapeColor,
                                                    clickedShape.secondaryShapeColor,
                                                    clickedShape.shapeShadingType,
                                                    StartAndEndPointMode.MOVE);
            int deltaX = this.mousePoint.x - this.mouseDragPoint.x;
            int deltaY = this.mousePoint.y - this.mouseDragPoint.y;
            ShapeAdapter shape = adapter.convert(new Dimensions(
                                                 new Point(clickedShape.x - deltaX, 
                                                           clickedShape.y - deltaY),
                                                 new Point(clickedShape.width, 
                                                           clickedShape.height)
                                            )
            );
            this.appState.setDraggedShape(shape);
        }
        this.setMouseDraggedPosition(e.getPoint());
    }

    @Override
    public void mouseReleasedMove(MouseEvent e) {
        ShapeAdapter draggedShape = this.appState.getDraggedShape();
        if (draggedShape != null && this.appState.getClickedShape() != null)
        {
            //this.appState.removeShapeFromBuffer(this.appState.getClickedShape());
            //this.appState.addShapeAttribute(draggedShape);
            this.appState.move(this.appState.getClickedShape(), draggedShape);
        }
        this.setMousePosition(e.getPoint());
        this.appState.resetDraggedShape();
    }

    @Override
    public void mousePressedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(e.getPoint());
        this.appState.setClickedShape(e.getPoint());
        this.appState.setSelectedShape(this.appState.getClickedShape());
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
        this.appState.setDraggedShape(shape);
    }

    @Override
    public void mouseReleasedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.appState.resetDraggedShape();
    }
}

