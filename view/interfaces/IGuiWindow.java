package view.interfaces;
import model.*;

import view.EventName;
import view.gui.PaintCanvas;

import javax.swing.*;

public interface IGuiWindow {
	void addEvent(EventName eventName, IEventCallback callback);
    <T> T getDialogResponse(IDialogChoice dialogSettings);
    JButton getButton(EventName eventName);
    void setStatusMenu(JPaintShape shape);
    PaintCanvas getCanvas();
}
