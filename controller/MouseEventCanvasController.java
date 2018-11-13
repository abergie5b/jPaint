package controller;

import model.*;
import model.interfaces.IApplicationState;
import model.persistence.JPaintShapeAdapterFactory;

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
        JPaintShapeAdapter adapter = JPaintShapeAdapterFactory.Create(this.appState,
                                                                      this.mousePoint,
                                                                      this.mouseDragPoint,
                                                                      SwingUtilities.isRightMouseButton(e));
        this.appState.setDraggedShape(adapter);
        this.appState.repaint();
    }

    @Override
    public void mouseReleasedDraw(MouseEvent e) {
        JPaintShapeAdapter draggedShape = this.appState.getDraggedShape();
        this.appState.addShape(draggedShape);
        this.setMousePosition(e.getPoint());
        this.appState.setDraggedShape(null);
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
            JPaintShapeAdapter adapter = JPaintShapeAdapterFactory.Create(this.appState,
                                                                          this.mousePoint,
                                                                          this.mouseDragPoint);
            this.appState.setDraggedShape(adapter);
            this.appState.repaint();
        }
        this.setMouseDraggedPosition(e.getPoint());
    }

    @Override
    public void mouseReleasedMove(MouseEvent e) {
        this.appState.move(this.appState.getClickedShape(),
                           this.appState.getDraggedShape());
        this.setMousePosition(e.getPoint());
        this.appState.setDraggedShape(null);
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
        this.setMouseDraggedPosition(e.getPoint());
        JPaintShapeAdapter adapter = JPaintShapeAdapterFactory.Create(this.appState,
                                                                      this.mousePoint,
                                                                      this.mouseDragPoint);
        this.appState.setDraggedShape(adapter);
        this.appState.repaint();
    }

    @Override
    public void mouseReleasedSelect(MouseEvent e) {
        this.setMousePosition(e.getPoint());
        this.appState.setDraggedShape(null);
        this.appState.repaint();
    }
}

