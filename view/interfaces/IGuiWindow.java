package view.interfaces;
import model.StateModel;

import view.EventName;

import javax.swing.*;

public interface IGuiWindow {
    JButton getButton(EventName eventName);
    void setStatusMenu();
    void setStateModel(StateModel shapeType);
}
