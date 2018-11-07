package model.persistence;

import model.*;
import model.dialogs.DialogProvider;
import model.interfaces.IApplicationState;
import model.interfaces.IDialogProvider;
import view.interfaces.IGuiWindow;
import view.gui.PaintCanvas;

import java.util.ArrayList;
import java.io.Serializable;

import java.awt.*;
import java.awt.geom.*;

public class ApplicationState implements IApplicationState, Serializable 
{
    private static final long serialVersionUID = -5545483996576839007L;
    private final IGuiWindow uiModule;
    private PaintCanvas canvas;
    private final IDialogProvider dialogProvider;

    private ShapeAdapter displaySettingsShape;
    private ShapeType activeShapeType;
    private ShapeColor activePrimaryColor;
    private ShapeColor activeSecondaryColor;
    private ShapeShadingType activeShapeShadingType;
    private StartAndEndPointMode activeStartAndEndPointMode;

    public ShapeAdapter clickedShape;
    public ShapeAdapter draggedShape;
    public ArrayList<ShapeAdapter> selectedShapes;
    private ArrayList<ShapeAdapter> shapes;
    private CommandHistory commandHistory;
    private ArrayList<ShapeAdapter> copiedShapes;

    public ApplicationState(IGuiWindow uiModule) 
    {
        this.uiModule = uiModule;
        this.canvas = uiModule.getCanvas();
        this.dialogProvider = new DialogProvider(this);
        this.shapes = new ArrayList<ShapeAdapter>();
        this.commandHistory = new CommandHistory();
        this.selectedShapes = new ArrayList<ShapeAdapter>();
        this.clickedShape = null;
        this.draggedShape = null;
        this.copiedShapes = new ArrayList<ShapeAdapter>();
        /* Defaults */
        setDefaults();
        displaySettingsShape = new ShapeAdapter(activeShapeType, 
                                                activePrimaryColor,
                                                activeSecondaryColor, 
                                                activeShapeShadingType,
                                                activeStartAndEndPointMode
        );
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
    }

    private class CommandHistory 
    {
        public int pointer;
        public ArrayList<ICommand> commands;
        private ArrayList<Integer> nullifiedCommands;
        private CommandHistory()
        {
            this.pointer = 0;
            this.commands = new ArrayList<ICommand>();
            this.nullifiedCommands = new ArrayList<Integer>();
        }
        public void add(ICommand command)
        {
            this.commands.add(command);
            this.pointer = this.commands.size() - 1;
        }
        public ICommand getUndoCommand()
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
        public ICommand getRedoCommand()
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

    @Override
    public void addShapeAttribute(ShapeAdapter _shape) 
    {
        AddShapeCommand addShape = new AddShapeCommand(this.shapes, _shape);
        commandHistory.add(addShape);
        addShape.execute();
    }

    public void redo() 
    {
        ICommand lastCommand = commandHistory.getRedoCommand();
        lastCommand.execute();
        this.repaint();
    }

    public void undo() 
    {
        ICommand lastCommand = commandHistory.getUndoCommand();
        lastCommand.undo();
        this.repaint();
    }

    public void delete()
    {
        DeleteCommand delete = new DeleteCommand(this.shapes, this.selectedShapes);
        commandHistory.add(delete);
        delete.execute();
        this.repaint();
    }

    public void move(ShapeAdapter to, ShapeAdapter from)
    {
        MoveCommand move = new MoveCommand(this.shapes, to, from);
        commandHistory.add(move);
        move.execute();
    }

    @Override
    public void copy() {
        this.copiedShapes = this.selectedShapes;
    }

    @Override
    public void paste() {
        PasteCommand paste = new PasteCommand(shapes, copiedShapes);
        commandHistory.add(paste);
        paste.execute();
        repaint();
    }

    @Override
    public void repaint()
    {
        this.canvas.repaintCanvas(shapes, this.draggedShape); // refactor
    }

    @Override
    public ShapeAdapter getDraggedShape()
    {
        return this.draggedShape;
    }
    
    @Override
    public ShapeAdapter getClickedShape()
    {
        return this.clickedShape;
    }

    @Override
    public ArrayList<ShapeAdapter> getSelectedShapes()
    {
        return this.selectedShapes;
    }

    @Override
    public void setDraggedShape(ShapeAdapter shape)
    {
        this.draggedShape = shape;
        this.repaint();
    }

    @Override
    public void resetDraggedShape()
    {
        this.draggedShape = null;
        this.repaint();
    }

    @Override
    public void setClickedShape(Point point)
    {
        this.clickedShape = this.getShapeFromBuffer(point);
    }

    @Override
    public ArrayList<ShapeAdapter> getShapesinSelection(Rectangle selection)
    {
        ArrayList<ShapeAdapter> selectedShapes = new ArrayList<ShapeAdapter>();
        for (ShapeAdapter s: this.shapes) 
        {
            if (s.shape.intersects(selection))
            {
                selectedShapes.add(s);
            }
        }
        return selectedShapes;
    }

    @Override
    public ShapeAdapter getShapeFromBuffer(Point point) 
    {
        ShapeAdapter _shape = null;
        for (ShapeAdapter s: this.shapes)
        {
            if (s.shape.contains(point))
            {
                _shape = s;
            }
        }
        return _shape;
    }

    public void setSelectedShape(ShapeAdapter _shape) {
        ArrayList<ShapeAdapter>_shapes = new ArrayList<ShapeAdapter>();
        _shapes.add(_shape);
        this.selectedShapes = _shapes;
    }

    @Override 
    public void setSelectedShapes(Rectangle selection) {
        this.selectedShapes = this.getShapesinSelection(selection);
    }

    @Override
    public void setActiveShape() {
        activeShapeType = uiModule.getDialogResponse(dialogProvider.getChooseShapeDialog());
        displaySettingsShape.setShapeType(activeShapeType);
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
    }

    private void updatePrimaryColorInSelectMode(ShapeColor color)
    {
        for (ShapeAdapter s: this.getSelectedShapes())
        {
            s.updatePrimaryColor(color);
        }
    }

    private void updateSecondaryColorInSelectMode(ShapeColor color)
    {
        for (ShapeAdapter s: this.getSelectedShapes())
        {
            s.updateSecondaryColor(color);
        }
    }

    private void updateShadingTypeInSelectMode(ShapeShadingType shading)
    {
        for (ShapeAdapter s: this.getSelectedShapes())
        {
            s.updateShadingType(shading);
        }
    }

    @Override
    public void setActivePrimaryColor() {
        activePrimaryColor = uiModule.getDialogResponse(dialogProvider.getChoosePrimaryColorDialog());
        displaySettingsShape.updatePrimaryColor(activePrimaryColor);
        if (this.activeStartAndEndPointMode == StartAndEndPointMode.SELECT)
        {
            updatePrimaryColorInSelectMode(activePrimaryColor);
            this.repaint();
        }
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveSecondaryColor() {
        activeSecondaryColor = uiModule.getDialogResponse(dialogProvider.getChooseSecondaryColorDialog());
        displaySettingsShape.updateSecondaryColor(activeSecondaryColor);
        if (this.activeStartAndEndPointMode == StartAndEndPointMode.SELECT)
        {
            updateSecondaryColorInSelectMode(activeSecondaryColor);
            this.repaint();
        }
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveShadingType() {
        activeShapeShadingType = uiModule.getDialogResponse(dialogProvider.getChooseShadingTypeDialog());
        displaySettingsShape.shapeShadingType = activeShapeShadingType;
        if (this.activeStartAndEndPointMode == StartAndEndPointMode.SELECT)
        {
            updateShadingTypeInSelectMode(activeShapeShadingType);
            this.repaint();
        }
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveStartAndEndPointMode() {
        activeStartAndEndPointMode = uiModule.getDialogResponse(dialogProvider.getChooseStartAndEndPointModeDialog());
        displaySettingsShape.startAndEndPointMode = activeStartAndEndPointMode;
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
    }

    @Override
    public ShapeType getActiveShapeType() {
        return activeShapeType;
    }

    @Override
    public ShapeColor getActivePrimaryColor() {
        return activePrimaryColor;
    }

    @Override
    public ShapeColor getActiveSecondaryColor() {
        return activeSecondaryColor;
    }

    @Override
    public ShapeShadingType getActiveShapeShadingType() {
        return activeShapeShadingType;
    }

    @Override
    public StartAndEndPointMode getActiveStartAndEndPointMode() {
        return activeStartAndEndPointMode;
    }

    private void setDefaults() {
        activeShapeType = ShapeType.TRIANGLE;
        activePrimaryColor = ShapeColor.LIGHT_GRAY;
        activeSecondaryColor = ShapeColor.GREEN;
        activeShapeShadingType = ShapeShadingType.OUTLINE_AND_FILLED_IN;
        activeStartAndEndPointMode = StartAndEndPointMode.DRAW;
    }
}
