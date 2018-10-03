package view.gui;

import javax.swing.*;

import java.awt.geom.*;
import java.awt.*;

import view.EventName;
import view.interfaces.IDialogChoice;
import view.interfaces.IEventCallback;
import view.interfaces.IGuiWindow;
import view.interfaces.IUiModule;

import model.*;

public class Gui implements IUiModule 
{

    private final IGuiWindow gui;

    public Gui(IGuiWindow gui) 
    {
        this.gui = gui;
    }

    @Override 
    public PaintCanvas getCanvas() {
        return gui.getCanvas();
    }

    @Override
    public void setStatusMenu() {
        gui.setStatusMenu();
    }

    @Override
    public void setShape(ShapeAdapter stateModel) {
        gui.setShape(stateModel);
    }
    
	@Override
	public void addEvent(EventName eventName, IEventCallback callback) {
		JButton button = gui.getButton(eventName);
		button.addActionListener((ActionEvent) -> callback.run());
	}

    @Override
    public <T> T getDialogResponse(IDialogChoice dialogSettings) 
    {
        Object selectedValue = JOptionPane.showInputDialog(null,
                                                           dialogSettings.getDialogText(), 
                                                           dialogSettings.getDialogTitle(),
                                                           JOptionPane.PLAIN_MESSAGE,
                                                           null,
                                                           dialogSettings.getDialogOptions(),
                                                           dialogSettings.getCurrentSelection()
        );
        return selectedValue == null
                ? (T)dialogSettings.getCurrentSelection()
                : (T)selectedValue;
    }
}
