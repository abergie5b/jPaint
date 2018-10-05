package view.interfaces;
import model.*;

import view.EventName;
import view.gui.PaintCanvas;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

public interface IGuiWindow {
	public void addEvent(EventName eventName, IEventCallback callback);
    public <T> T getDialogResponse(IDialogChoice dialogSettings);
    JButton getButton(EventName eventName);
    void setStatusMenu();
    void setShape(ShapeAdapter shapeType);
    PaintCanvas getCanvas();
}
