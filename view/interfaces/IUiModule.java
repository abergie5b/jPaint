package view.interfaces;

import model.*;

import controller.MouseEventListener;

import view.gui.PaintCanvas;
import view.EventName;

import javax.swing.event.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public interface IUiModule {
    void addEvent(EventName eventName, IEventCallback command);
    void setStatusMenu();
    void setStateModel(StateModel shapeType);
    void addMouseListeners(MouseEventListener mouseListener);
    void setCanvasShape(Shape s);
    void setShapeColor(Color c);
    void setShapeSecondaryColor(Color c);
    void setShapeShading(ShapeShadingType shading);
    void setStartAndEndPointMode(StartAndEndPointMode mode);
    void updateCanvasSettings(StateModel stateModel);
    PaintCanvas getCanvas();
    <T> T getDialogResponse(IDialogChoice dialogChoice);
}
