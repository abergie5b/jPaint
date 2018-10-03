package view.interfaces;

import model.*;

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
    void setShape(ShapeAdapter shapeType);
    PaintCanvas getCanvas();
    <T> T getDialogResponse(IDialogChoice dialogChoice);
}
