package controller;

import model.*;
import model.interfaces.IApplicationState;
import view.gui.PaintCanvas;

import java.awt.*;
import javax.swing.*;
import java.lang.Math;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;

public class MouseEventController implements IMouseEventController
{
    private final PaintCanvas canvas;
    private final IApplicationState applicationState;
    private Point mousePoint;
    private Point mouseDragPoint;
    
    // TODO move these to ApplicationState
    private ShapeAdapter clickedShape;
    private ShapeAdapter draggedShape;
    private ArrayList<ShapeAdapter> selectedShapes;

    public MouseEventController(PaintCanvas canvas, IApplicationState applicationState) 
    { 
        this.canvas = canvas;
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

    private void setDraggedShape(ShapeAdapter shape)
    {
        // TODO move this to ApplicationState
        this.draggedShape = shape;
    }

    private void resetDraggedShape()
    {
        // TODO move this to ApplicationState
        this.draggedShape = null;
    }

    private void setClickedShape(Point point)
    {
        // TODO move this to ApplicationState
        this.clickedShape = this.canvas.getShapeFromBuffer(point);
        if (this.clickedShape != null)
        {
            System.out.println("Found shape from buffer: " + this.clickedShape.shape + " " + this.clickedShape.x + " " + this.clickedShape.y + " " + this.clickedShape.width + " " + this.clickedShape.height);
        }
    }

    @Override
    public void mousePressedDraw(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(e.getPoint());
        this.setClickedShape(e.getPoint());
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
        this.canvas.setTempShape(adapter);
        this.setDraggedShape(adapter);
        this.canvas.repaint();
    }

    @Override
    public void mouseReleasedDraw(MouseEvent e) {
        if (this.draggedShape != null)
        {
            this.canvas.addShapeAttribute(this.draggedShape);
        }
        this.canvas.repaint();
        this.setMousePosition(e.getPoint());
        
        // TODO move this to ApplicationState
        this.resetDraggedShape();
    }

    @Override
    public void mousePressedMove(MouseEvent e) {
        Point point = e.getPoint();
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(e.getPoint());
        
        // TODO move this to ApplicationState
        this.setClickedShape(point);
    }

    @Override
    public void mouseDraggedMove(MouseEvent e) {
        if (this.clickedShape != null)
        {
            ShapeAdapter adapter = new ShapeAdapter(this.clickedShape.shapeType,
                                                    this.clickedShape.primaryShapeColor,
                                                    this.clickedShape.secondaryShapeColor,
                                                    this.clickedShape.shapeShadingType,
                                                    StartAndEndPointMode.MOVE);
            int deltaX = this.mousePoint.x - this.mouseDragPoint.x;
            int deltaY = this.mousePoint.y - this.mouseDragPoint.y;
            adapter.setShape(new Dimensions(new Point(this.clickedShape.x - deltaX, this.clickedShape.y - deltaY),
                                            new Point(this.clickedShape.width, this.clickedShape.height))
            );
            this.setDraggedShape(adapter);
            this.canvas.setTempShape(adapter);
            this.canvas.repaint();
        }
        this.setMouseDraggedPosition(e.getPoint());
    }

    @Override
    public void mouseReleasedMove(MouseEvent e) {
        if (this.draggedShape != null && this.clickedShape != null)
        {
            this.canvas.removeShapeFromBuffer(this.clickedShape);
            this.canvas.addShapeAttribute(this.draggedShape);
        }
        this.canvas.repaint();
        this.setMousePosition(e.getPoint());
        
        // TODO move this to ApplicationState
        this.resetDraggedShape();
    }

    @Override
    public void mousePressedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(e.getPoint());
        
        // TODO move this to ApplicationState
        this.setClickedShape(e.getPoint());
        this.canvas.repaint();
    }

    @Override
    public void mouseDraggedSelect(MouseEvent e) {
        Dimensions dims = Geometry.getDimensionsWithInvert(this.mousePoint, this.mouseDragPoint);
        Rectangle selectionRectangle = new Rectangle(dims.xy.x, dims.xy.y, dims.wh.x, dims.wh.y);
        this.selectedShapes = this.canvas.getShapesinSelection(selectionRectangle);
        ShapeAdapter adapter = new ShapeAdapter(ShapeType.RECTANGLE,
                                                ShapeColor.BLACK,
                                                ShapeColor.BLACK,
                                                ShapeShadingType.OUTLINE,
                                                StartAndEndPointMode.SELECT);
        adapter.setShape(dims);
        this.setMouseDraggedPosition(e.getPoint());

        // TODO move this to ApplicationState
        this.canvas.setTempShape(adapter);
        this.canvas.repaint();
    }

    @Override
    public void mouseReleasedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.applicationState.setSelectedShapes(this.selectedShapes);
        this.canvas.repaint();
    }
}

