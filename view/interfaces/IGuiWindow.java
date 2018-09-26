package view.interfaces;
import model.*;

import view.EventName;
import view.gui.PaintCanvas;

import controller.MouseEventListener;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

public interface IGuiWindow {
    JButton getButton(EventName eventName);
    void setStatusMenu();
    void setStateModel(StateModel shapeType);
    void setCanvasShape(Shape s);
    void setShapeColor(Color color);
    void setShapeSecondaryColor(Color color);
    void setShapeShading(ShapeShadingType shading);
    void setStartAndEndPointMode(StartAndEndPointMode mode);
    PaintCanvas getCanvas();
    void addMouseListeners(MouseEventListener mouseListener);
}
