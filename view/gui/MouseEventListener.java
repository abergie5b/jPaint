package view.gui;

import model.*;
import model.interfaces.IApplicationState;

import view.interfaces.IUiModule;

import controller.MouseEventController;

import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

public class MouseEventListener extends MouseInputAdapter implements IMouseEventListener {
    private int mouseX;
    private int mouseY;
    private int moveX;
    private int moveY;
    private final IApplicationState applicationState;
    private final IUiModule uiModule;
    private StateModelAdapter clickedShape;
    private StateModelAdapter draggedShape;
    private MouseEventController mouseEventController;

    public MouseEventListener(IUiModule uiModule, IApplicationState applicationState) 
    { 
        this.uiModule = uiModule;
        this.applicationState = applicationState;
        this.mouseEventController = new MouseEventController(uiModule, applicationState);
        this.mouseX = 0;
        this.mouseY = 0;
        this.moveX = 0;
        this.moveY = 0;
    } 

    @Override
    public void mousePressed(MouseEvent evt) {
        int startX = evt.getX();
        int startY = evt.getY();
        StartAndEndPointMode mode = applicationState.getActiveStartAndEndPointMode();
        switch (mode)
        {
            case DRAW:
                mouseEventController.mousePressedDraw(startX, startY);
                break;
            case MOVE:
                mouseEventController.mousePressedMove(evt.getPoint());
                break;
            case SELECT:
                mouseEventController.mousePressedSelect(startX, startY);
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
                mouseEventController.mouseDraggedDraw(endX, endY);
                break;
            case MOVE:
                mouseEventController.mouseDraggedMove(endX, endY);
                break;
            case SELECT:
                mouseEventController.mouseDraggedSelect(endX, endY);
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        int endX = evt.getX();
        int endY = evt.getY();
        StartAndEndPointMode mode = applicationState.getActiveStartAndEndPointMode();
        switch (mode)
        {
            case DRAW:
                mouseEventController.mouseReleasedDraw(endX, endY);
                break;
            case MOVE:
                mouseEventController.mouseReleasedMove(endX, endY);
                break;
            case SELECT:
                mouseEventController.mouseReleasedSelect(endX, endY);
                break;
        }
    }
}

