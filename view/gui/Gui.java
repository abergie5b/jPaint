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

    
}
