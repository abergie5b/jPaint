package controller;

import model.*;
import model.interfaces.IApplicationState;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

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
        JPaintShapeAdapter adapter;
        if (SwingUtilities.isRightMouseButton(e))
        {
            adapter = this.appState.getMouseDraggedDrawShape(this.mousePoint, this.mouseDragPoint, true);
        }
        else
        {
            adapter = this.appState.getMouseDraggedDrawShape(this.mousePoint, this.mouseDragPoint, false);
        }
        this.appState.setDraggedShape(adapter);
        this.appState.repaint();
    }

    @Override
    public void mouseReleasedDraw(MouseEvent e) {
        JPaintShapeAdapter draggedShape = this.appState.getDraggedShape();
        this.appState.addShapeAttribute(draggedShape);
        this.setMousePosition(e.getPoint());
        this.appState.resetDraggedShape();
        this.appState.repaint();
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
        if (this.appState.getClickedShape() != null)
        {
            JPaintShapeAdapter adapter = this.appState.getMouseDraggedMoveShape(this.mousePoint, this.mouseDragPoint);
            this.appState.setDraggedShape(adapter);
            this.appState.repaint();
        }
        this.setMouseDraggedPosition(e.getPoint());
    }

    @Override
    public void mouseReleasedMove(MouseEvent e) {
        JPaintShapeAdapter draggedShape = this.appState.getDraggedShape();
        JPaintShapeAdapter clickedShape = this.appState.getClickedShape();
        this.appState.move(clickedShape, draggedShape);
        this.setMousePosition(e.getPoint());
        this.appState.resetDraggedShape();
        this.appState.repaint();
    }

    @Override
    public void mousePressedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.setMouseDraggedPosition(e.getPoint());
        this.appState.setClickedShape(e.getPoint());
        this.appState.setSelectedShape();
        this.appState.repaint();
    }

    @Override
    public void mouseDraggedSelect(MouseEvent e) {
        Dimensions dims = Geometry.getDimensionsWithInvert(this.mousePoint, this.mouseDragPoint);
        Rectangle selectionRectangle = new Rectangle(dims.xy.x, dims.xy.y, dims.wh.x, dims.wh.y);
        this.appState.setSelectedShapesFromRectangle(selectionRectangle);
        JPaintShape shape = new JPaintShape(ShapeType.RECTANGLE,
                                            ShapeColor.BLACK,
                                            ShapeColor.BLACK,
                                            ShapeShadingType.OUTLINE,
                                            StartAndEndPointMode.SELECT);
        JPaintShapeAdapter adapter = new JPaintShapeAdapter(shape, dims);
        this.setMouseDraggedPosition(e.getPoint());
        this.appState.setDraggedShape(adapter);
        this.appState.repaint();
    }

    @Override
    public void mouseReleasedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.appState.resetDraggedShape();
        this.appState.repaint();
    }
}

