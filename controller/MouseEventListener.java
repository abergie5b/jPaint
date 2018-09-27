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

public class MouseEventListener extends MouseInputAdapter implements IMouseEventListener {
    private int mouseX;
    private int mouseY;
    private final IApplicationState applicationState;
    private final IUiModule uiModule;

    public MouseEventListener(IUiModule uiModule, IApplicationState applicationState) 
    { 
        this.uiModule = uiModule;
        this.applicationState = applicationState;
        this.mouseX = 0;
        this.mouseY = 0;
    } 

    @Override
    public void mousePressed(MouseEvent evt) {
        int startX = evt.getX();
        int startY = evt.getY();
        mouseX = startX;
        mouseY = startY;
        Shape s = uiModule.getCanvas().getShapeFromBuffer(evt.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        int endX = evt.getX();
        int endY = evt.getY();
        StartAndEndPointMode mode = applicationState.getActiveStartAndEndPointMode();
        switch (mode) 
        {
            case DRAW:  
            {
                PaintCanvas canvas  = uiModule.getCanvas();
                StateModelAdapter adapter = new StateModelAdapter(applicationState.getActiveShapeType(),
                                                                  applicationState.getActivePrimaryColor(),
                                                                  applicationState.getActiveSecondaryColor(),
                                                                  applicationState.getActiveShapeShadingType(),
                                                                  applicationState.getActiveStartAndEndPointMode()
                );
                adapter.setShape(mouseX, mouseY, endX, endY);
                canvas.setTempShape(adapter);
                canvas.repaint();
            }
            case SELECT:
                break;
            case MOVE:
                break;
        }
        //mouseX = endX;
        //mouseY = endY;
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        int endX = evt.getX();
        int endY = evt.getY();
        PaintCanvas canvas  = uiModule.getCanvas();
        StateModelAdapter adapter = new StateModelAdapter(applicationState.getActiveShapeType(),
                                                          applicationState.getActivePrimaryColor(),
                                                          applicationState.getActiveSecondaryColor(),
                                                          applicationState.getActiveShapeShadingType(),
                                                          applicationState.getActiveStartAndEndPointMode()
        );
        adapter.setShape(mouseX, mouseY, endX, endY);
        canvas.addShapeAttribute(adapter);
        canvas.repaint();
        mouseX = endX;
        mouseY = endY;
    }
}

