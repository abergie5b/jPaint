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
    void setStateModel(StateModelAdapter shapeType);
    PaintCanvas getCanvas();
    <T> T getDialogResponse(IDialogChoice dialogChoice);
}
