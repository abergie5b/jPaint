package view.gui;

import model.*;
import model.interfaces.IApplicationState;
import controller.MouseEventController;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.event.*;

public class MouseEventListener extends MouseInputAdapter implements IMouseEventListener {
    private final IApplicationState applicationState;
    private MouseEventController mouseEventController;

    public MouseEventListener(IApplicationState applicationState)
    { 
        this.applicationState = applicationState;
        this.mouseEventController = new MouseEventController(applicationState);
    } 

    @Override
    public void mousePressed(MouseEvent evt) {
        StartAndEndPointMode mode = applicationState.getActiveStartAndEndPointMode();
        mouseEventController.printEvent("Pressed");
        switch (mode)
        {
            case DRAW:
                mouseEventController.mousePressedDraw(evt);
                break;
            case MOVE:
                mouseEventController.mousePressedMove(evt);
                break;
            case SELECT:
                mouseEventController.mousePressedSelect(evt);
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        StartAndEndPointMode mode = applicationState.getActiveStartAndEndPointMode();
        mouseEventController.printEvent("Dragged");
        switch (mode) 
        {
            case DRAW:  
                mouseEventController.mouseDraggedDraw(evt);
                break;
            case MOVE:
                mouseEventController.mouseDraggedMove(evt);
                break;
            case SELECT:
                mouseEventController.mouseDraggedSelect(evt);
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        StartAndEndPointMode mode = applicationState.getActiveStartAndEndPointMode();
        mouseEventController.printEvent("Release");
        switch (mode)
        {
            case DRAW:
                mouseEventController.mouseReleasedDraw(evt);
                break;
            case MOVE:
                mouseEventController.mouseReleasedMove(evt);
                break;
            case SELECT:
                mouseEventController.mouseReleasedSelect(evt);
                break;
        }
    }
}

