package view.interfaces;
import model.*;

import view.EventName;
import view.gui.PaintCanvas;

import javax.swing.*;

public interface IGuiWindow {
	public void addEvent(EventName eventName, IEventCallback callback);
    public <T> T getDialogResponse(IDialogChoice dialogSettings);
    JButton getButton(EventName eventName);
    void setStatusMenu();
    void setShape(JPaintShape shapeType);
    PaintCanvas getCanvas();
}
