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
    private StateModelAdapter clickedShape;

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
        StartAndEndPointMode mode = applicationState.getActiveStartAndEndPointMode();
        PaintCanvas canvas = uiModule.getCanvas();
        switch (mode)
        {
            case DRAW:
                break;
            case MOVE:
            {
                clickedShape = canvas.getShapeFromBuffer(evt.getPoint());
                if (clickedShape != null)
                {
                    System.out.println("Found shape from buffer: " + clickedShape.shape + " " + clickedShape);
                }
                else 
                {
                    System.out.println("Could not find shape");
                }
            }
            case SELECT:
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
            {
                PaintCanvas canvas  = uiModule.getCanvas();
                StateModelAdapter adapter = new StateModelAdapter(applicationState.getActiveShapeType(),
                                                                  applicationState.getActivePrimaryColor(),
                                                                  applicationState.getActiveSecondaryColor(),
                                                                  applicationState.getActiveShapeShadingType(),
                                                                  mode
                );
                adapter.setShape(mouseX, mouseY, endX, endY);
                canvas.setTempShape(adapter);
                canvas.repaint();
            }
            case MOVE:
            {
                if (clickedShape != null)
                {
                    System.out.println("Mouse dragged with shape: " + clickedShape.shape + " " + clickedShape);
                    PaintCanvas canvas  = uiModule.getCanvas();
                    StateModelAdapter adapter = new StateModelAdapter(clickedShape.shapeType,
                                                                      clickedShape.primaryShapeColor,
                                                                      clickedShape.secondaryShapeColor,
                                                                      clickedShape.shapeShadingType,
                                                                      mode
                    );
                    adapter.setShape(clickedShape.x + endX, clickedShape.y + endY, clickedShape.width, clickedShape.height);
                    canvas.setTempShape(adapter);
                    canvas.repaint();
                }
            }
            case SELECT:
                break;
        }
        //mouseX = endX;
        //mouseY = endY;
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
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
                                                                  mode
                );
                adapter.setShape(mouseX, mouseY, endX, endY);
                canvas.addShapeAttribute(adapter);
                canvas.repaint();
            }
            case MOVE:
                break;
            case SELECT:
                break;
        }
        mouseX = endX;
        mouseY = endY;
    }
}

