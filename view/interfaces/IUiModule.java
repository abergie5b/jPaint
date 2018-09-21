package view.interfaces;

import model.StateModel;
import view.EventName;

public interface IUiModule {
    void addEvent(EventName eventName, IEventCallback command);
    void setStatusMenu(StateModel shapeType);
    <T> T getDialogResponse(IDialogChoice dialogChoice);
}
