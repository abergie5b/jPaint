package model.persistence;

import model.*;
import model.dialogs.DialogProvider;
import model.interfaces.IApplicationState;
import model.interfaces.IDialogProvider;
import view.interfaces.IGuiWindow;
import view.gui.PaintCanvas;
import console.Console;

import java.util.ArrayList;
import java.io.Serializable;

import java.awt.*;

public class ApplicationState implements IApplicationState, Serializable 
{
    private static final long serialVersionUID = -5545483996576839007L;
    private final IGuiWindow uiModule;
    private PaintCanvas canvas;
    private final IDialogProvider dialogProvider;

    private ArrayList<JPaintShapeAdapter> shapes;
    private CommandHistory commandHistory;
    private ArrayList<JPaintShapeAdapter> selectedShapes;
    private ArrayList<JPaintShapeAdapter> copiedShapes;
    private JPaintShapeAdapter clickedShape;
    private JPaintShapeAdapter draggedShape;

    private ShapeType activeShapeType;
    private ShapeColor activePrimaryColor;
    private ShapeColor activeSecondaryColor;
    private ShapeShadingType activeShapeShadingType;
    private StartAndEndPointMode activeStartAndEndPointMode;

    public ApplicationState(IGuiWindow uiModule)
    {
        /* GUI Objects */
        this.uiModule = uiModule;
        this.canvas = uiModule.getCanvas();
        this.dialogProvider = new DialogProvider(this);
        /* State Variables */
        this.shapes = new ArrayList<>();
        this.commandHistory = new CommandHistory();
        this.selectedShapes = new ArrayList<>();
        this.copiedShapes = new ArrayList<>();
        this.clickedShape = null;
        this.draggedShape = null;
        /* Defaults */
        setDefaults();
        this.setUIStatusMenu();
    }

    private class CommandHistory
    {
        int pointer;
        ArrayList<ICommand> commands;
        private ArrayList<Integer> nullifiedCommands; // TODO
        private CommandHistory()
        {
            this.pointer = 0;
            this.commands = new ArrayList<>();
            this.nullifiedCommands = new ArrayList<>();
        }
        void add(ICommand command)
        {
            this.commands.add(command);
            this.pointer = this.commands.size() - 1;
        }
        ICommand getUndoCommand()
        {
            ICommand command = null;
            if (this.pointer >= 0)
            {
                command = this.commands.get(this.pointer);
                nullifiedCommands.add(this.pointer);
                this.pointer--;
            }
            return command;
        }
        ICommand getRedoCommand()
        {
            ICommand command = null;
            if (this.pointer < (this.commands.size() - 1))
            {
                this.pointer++;
                command = this.commands.get(this.pointer);
            }
            return command;
        }
    }

    public void showConsole() {
        Console console = new Console(this);
        console.showTextArea();
    }

    @Override
    public ArrayList<JPaintShapeAdapter> getShapes()
    {
        return this.shapes;
    }

    /**
     * @param _shape
     */
    @Override
    public void addShape(JPaintShapeAdapter _shape)
    {
        if (_shape != null) {
            ICommand addShape = CommandFactory.CreateAddShapeCommand(CommandType.AddShape,
                                                                     this.shapes,
                                                                     _shape);
            this.commandHistory.add(addShape);
            addShape.execute();
        }
    }

    @Override
    public void redo() 
    {
        ICommand lastCommand = this.commandHistory.getRedoCommand();
        lastCommand.execute();
        this.repaint();
    }

    @Override
    public void undo()
    {
        ICommand lastCommand = this.commandHistory.getUndoCommand();
        lastCommand.undo();
        this.repaint();
    }

    @Override
    public void delete()
    {
        ICommand delete = CommandFactory.CreateSelectionCommand(CommandType.Delete,
                                                                this.shapes,
                                                                this.selectedShapes);
        this.commandHistory.add(delete);
        delete.execute();
        this.repaint();
    }

    /**
     * @param from
     * @param to
     */
    @Override
    public void move(JPaintShapeAdapter from, JPaintShapeAdapter to)
    {
        if (from != null && to != null) {
            ICommand move = CommandFactory.CreateMoveCommand(CommandType.Move,
                                                             this.shapes,
                                                             from,
                                                             to);
            this.commandHistory.add(move);
            move.execute();
        }
    }

    @Override
    public void copy()
    {
        this.copiedShapes = this.selectedShapes;
    }

    @Override
    public void paste()
    {
        ICommand paste = CommandFactory.CreateSelectionCommand(CommandType.Paste,
                                                               this.shapes,
                                                               this.copiedShapes);
        this.commandHistory.add(paste);
        paste.execute();
        repaint();
    }

    @Override
    public void repaint()
    {
        this.canvas.repaintCanvas(this.shapes, this.draggedShape); // TODO
    }

    @Override
    public ArrayList<JPaintShapeAdapter> getSelectedShapes()
    {
        return this.selectedShapes;
    }

    @Override
    public void setSelectedShape()
    {
        ArrayList<JPaintShapeAdapter>_shapes = new ArrayList<>();
        _shapes.add(this.clickedShape);
        this.selectedShapes = _shapes;
    }

    /**
     * @param selection
     */
    @Override
    public void setSelectedShapesFromRectangle(Rectangle selection)
    {
        this.selectedShapes = this.getShapesInSelection(selection);
    }

    @Override
    public JPaintShapeAdapter getDraggedShape()
    {
        return this.draggedShape;
    }

    /**
     * @param shape
     */
    @Override
    public void setDraggedShape(JPaintShapeAdapter shape)
    {
        this.draggedShape = shape;
    }

    @Override
    public JPaintShapeAdapter getClickedShape()
    {
        return this.clickedShape;
    }

    /**
     * @param point
     */
    @Override
    public void setClickedShape(Point point)
    {
        this.clickedShape = this.getShapeFromBuffer(point);
    }

    private JPaintShapeAdapter getShapeFromBuffer(Point point)
    {
        JPaintShapeAdapter _shape = null;
        for (JPaintShapeAdapter s: this.shapes)
        {
            if (s.getShape().contains(point))
            {
                _shape = s;
            }
        }
        return _shape;
    }

    private ArrayList<JPaintShapeAdapter> getShapesInSelection(Rectangle selection)
    {
        ArrayList<JPaintShapeAdapter> selectedShapes = new ArrayList<>();
        for (JPaintShapeAdapter s: this.shapes)
        {
            if (s.getShape().intersects(selection))
            {
                selectedShapes.add(s);
            }
        }
        return selectedShapes;
    }

    private void setColorInSelectMode(CommandType type, ShapeColor color)
    {
        ICommand command = CommandFactory.CreateChangeColorCommand(type,
                                                                   color,
                                                                   this.selectedShapes);
        this.commandHistory.add(command);
        command.execute();
        this.repaint();
    }

    private void setShadingTypeInSelectMode(ShapeShadingType shading)
    {
        ICommand changeShadingType = CommandFactory.CreateChangeShadingCommand(CommandType.ChangeShadingType,
                                                                               shading,
                                                                               this.selectedShapes);
        this.commandHistory.add(changeShadingType);
        changeShadingType.execute();
        this.repaint();
    }

    @Override
    public void setActiveShape()
    {
        activeShapeType = uiModule.getDialogResponse(dialogProvider.getChooseShapeDialog());
        this.setUIStatusMenu();
    }

    @Override
    public void setActivePrimaryColor(ShapeColor color) {
        activePrimaryColor = color;
        if (this.activeStartAndEndPointMode == StartAndEndPointMode.SELECT)
        {
            setColorInSelectMode(CommandType.ChangePrimaryColor, activePrimaryColor);
            this.repaint();
        }
        this.setUIStatusMenu();
    }

    @Override
    public void setActivePrimaryColor()
    {
        activePrimaryColor = uiModule.getDialogResponse(dialogProvider.getChoosePrimaryColorDialog());
        if (this.activeStartAndEndPointMode == StartAndEndPointMode.SELECT)
        {
            setColorInSelectMode(CommandType.ChangePrimaryColor, activePrimaryColor);
            this.repaint();
        }
        this.setUIStatusMenu();
    }

    @Override
    public void setActiveSecondaryColor()
    {
        activeSecondaryColor = uiModule.getDialogResponse(dialogProvider.getChooseSecondaryColorDialog());
        if (this.activeStartAndEndPointMode == StartAndEndPointMode.SELECT)
        {
            setColorInSelectMode(CommandType.ChangeSecondaryColor, activeSecondaryColor);
            this.repaint();
        }
        this.setUIStatusMenu();
    }

    @Override
    public void setActiveShadingType()
    {
        activeShapeShadingType = uiModule.getDialogResponse(dialogProvider.getChooseShadingTypeDialog());
        if (this.activeStartAndEndPointMode == StartAndEndPointMode.SELECT)
        {
            setShadingTypeInSelectMode(activeShapeShadingType);
            this.repaint();
        }
        this.setUIStatusMenu();
    }

    @Override
    public void setActiveStartAndEndPointMode()
    {
        activeStartAndEndPointMode = uiModule.getDialogResponse(dialogProvider.getChooseStartAndEndPointModeDialog());
        this.setUIStatusMenu();
    }

    @Override
    public ShapeType getActiveShapeType()
    {
        return activeShapeType;
    }

    @Override
    public ShapeColor getActivePrimaryColor()
    {
        return activePrimaryColor;
    }

    @Override
    public ShapeColor getActiveSecondaryColor()
    {
        return activeSecondaryColor;
    }

    @Override
    public ShapeShadingType getActiveShapeShadingType()
    {
        return activeShapeShadingType;
    }

    @Override
    public StartAndEndPointMode getActiveStartAndEndPointMode()
    {
        return activeStartAndEndPointMode;
    }

    private void setUIStatusMenu()
    {
        uiModule.setStatusMenu(new JPaintShape(activeShapeType,
                activePrimaryColor,
                activeSecondaryColor,
                activeShapeShadingType,
                activeStartAndEndPointMode)
        );
    }

    private void setDefaults()
    {
        activeShapeType = ShapeType.TRIANGLE;
        activePrimaryColor = ShapeColor.LIGHT_GRAY;
        activeSecondaryColor = ShapeColor.GREEN;
        activeShapeShadingType = ShapeShadingType.OUTLINE_AND_FILLED_IN;
        activeStartAndEndPointMode = StartAndEndPointMode.DRAW;
    }
}
