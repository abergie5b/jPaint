package main;

import view.gui.MouseEventListener;
import controller.IJPaintController;
import controller.JPaintController;
import model.dialogs.DialogProvider;
import model.interfaces.IDialogProvider;
import model.persistence.ApplicationState;
import view.gui.Gui;
import view.gui.GuiWindow;
import view.gui.PaintCanvas;
import view.interfaces.IGuiWindow;

public class Main {
    public static void main(String[] args){
        PaintCanvas paintCanvas = new PaintCanvas();
        IGuiWindow guiWindow = new GuiWindow(paintCanvas);
        ApplicationState appState = new ApplicationState(guiWindow);
        MouseEventListener mouseEventListener = new MouseEventListener(appState);
        paintCanvas.addMouseMotionListener(mouseEventListener);
        paintCanvas.addMouseListener(mouseEventListener);
        IJPaintController controller = new JPaintController(guiWindow, appState);
        controller.setup();
    }
}
