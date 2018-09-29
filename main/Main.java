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
import view.interfaces.IUiModule;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args){
        PaintCanvas paintCanvas = new PaintCanvas();
        IGuiWindow guiWindow = new GuiWindow(paintCanvas);
        IUiModule uiModule = new Gui(guiWindow);
        ApplicationState appState = new ApplicationState(uiModule);
        MouseEventListener mouseEventListener = new MouseEventListener(uiModule, appState);
        paintCanvas.addMouseListeners(mouseEventListener);
        IJPaintController controller = new JPaintController(uiModule, appState);
        controller.setup();

        //Graphics2D graphics2d = paintCanvas.getGraphics2D();
        //graphics2d.setColor(Color.GREEN);
        //graphics2d.fillRect(12, 13, 200, 400);
        //graphics2d.setStroke(new BasicStroke(5));
        //graphics2d.setColor(Color.BLUE);
        //graphics2d.drawRect(12, 13, 200, 400);
    }
}
