package controller;

import model.*;
import model.interfaces.IApplicationState;
import view.EventName;
import view.interfaces.IGuiWindow;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.event.*;

public class JPaintController implements IJPaintController {
    private final IGuiWindow uiModule;
    private final IApplicationState applicationState;

    public JPaintController(IGuiWindow uiModule, IApplicationState applicationState) {
        this.uiModule = uiModule;
        this.applicationState = applicationState;
    }

    @Override
    public void setup() {
        setupEvents();
    }

    private void setupEvents() {
        uiModule.addEvent(EventName.CHOOSE_SHAPE, () -> applicationState.setActiveShape());
        uiModule.addEvent(EventName.CHOOSE_PRIMARY_COLOR, () -> applicationState.setActivePrimaryColor());
        uiModule.addEvent(EventName.CHOOSE_SECONDARY_COLOR, () -> applicationState.setActiveSecondaryColor());
        uiModule.addEvent(EventName.CHOOSE_SHADING_TYPE, () -> applicationState.setActiveShadingType());
        uiModule.addEvent(EventName.CHOOSE_START_POINT_ENDPOINT_MODE, () -> applicationState.setActiveStartAndEndPointMode());
        uiModule.addEvent(EventName.UNDO, () -> applicationState.undo());
        uiModule.addEvent(EventName.REDO, () -> applicationState.redo());
        uiModule.addEvent(EventName.DELETE, () -> applicationState.delete());
        uiModule.addEvent(EventName.PASTE, () -> applicationState.paste());
        uiModule.addEvent(EventName.COPY, () -> applicationState.copy());
    }
}
