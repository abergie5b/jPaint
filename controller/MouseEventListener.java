package controller;

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
    }
    @Override
    public void mouseReleased(MouseEvent evt) {
        int endX = evt.getX();
        int endY = evt.getY();
        StateModel stateModel = new StateModel(applicationState.getActiveShapeType(),
                                               applicationState.getActivePrimaryColor(),
                                               applicationState.getActiveSecondaryColor(),
                                               applicationState.getActiveShapeShadingType(),
                                               applicationState.getActiveStartAndEndPointMode()
        );
        uiModule.setCanvasShape(stateModel.shapeToObject(mouseX, mouseY, endX, endY));
        uiModule.updateCanvasSettings(stateModel);
        mouseX = endX;
        mouseY = endY;
        uiModule.getCanvas().repaint();
    }
}

