package controller;

import model.*;
import model.interfaces.IApplicationState;
import view.interfaces.IUiModule;
import view.gui.PaintCanvas;

import java.lang.Math;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class UserActionEventController
{

    public UserActionEventController(PaintCanvas canvas, IApplicationState applicationState) 
    {
        this.canvas = canvas;
        this.applicationState = applicationState;
    }

    public void undo() {
        canvas.undo();
    }

    public void redo() {
        canvas.redo();
    }

    public void delete() {
        canvas.deleteShapes(selectedShapes);
    }

    public void copy() {
    }

    public void paste() {
        for (ShapeAdapter s: applicationState.getSelectedShapes())
        {
            ShapeAdapter newShape = new ShapeAdapter(s.shapeType,
                                                               s.primaryShapeColor,
                                                               s.secondaryShapeColor,
                                                               s.shapeShadingType,
                                                               s.startAndEndPointMode
                                                               );
            newShape.setShape(0, 0, s.getWidth(), s.getHeight());
            canvas.addShapeAttribute(newShape);
        }
        canvas.repaint();
    }
}
