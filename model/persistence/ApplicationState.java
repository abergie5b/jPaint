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

    private JPaintShape displaySettingsShape;
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
        displaySettingsShape = new JPaintShape(activeShapeType,
                                               activePrimaryColor,
                                               activeSecondaryColor,
                                               activeShapeShadingType,
                                               activeStartAndEndPointMode);
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
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

    @Override
    public ArrayList<JPaintShapeAdapter> getShapes() {
        return this.shapes;
    }

    @Override
    public void addShapeAttribute(JPaintShapeAdapter _shape)
    {
        if (_shape != null) {
            ICommand addShape = CommandFactory.Create(CommandType.AddShape, this.shapes, _shape);
            commandHistory.add(addShape);
            addShape.execute();
        }
    }

    @Override
    public void redo() 
    {
        ICommand lastCommand = commandHistory.getRedoCommand();
        lastCommand.execute();
        this.repaint();
    }

    @Override
    public void undo()
    {
        ICommand lastCommand = commandHistory.getUndoCommand();
        lastCommand.undo();
        this.repaint();
    }

    @Override
    public void delete()
    {
        ICommand delete = CommandFactory.Create(CommandType.Delete, this.shapes, this.selectedShapes);
        commandHistory.add(delete);
        delete.execute();
        this.repaint();
    }

    @Override
    public void move(JPaintShapeAdapter from, JPaintShapeAdapter to)
    {
        if (from != null && to != null) {
            ICommand move = CommandFactory.Create(CommandType.Move, this.shapes, from, to);
            commandHistory.add(move);
            move.execute();
        }
    }

    @Override
    public void copy() {
        this.copiedShapes = this.selectedShapes;
    }

    @Override
    public void paste() {
        ICommand paste = CommandFactory.Create(CommandType.Paste, this.shapes, this.copiedShapes);
        commandHistory.add(paste);
        paste.execute();
        repaint();
    }

    @Override
    public void repaint()
    {
        this.canvas.repaintCanvas(this.shapes, this.draggedShape); // TODO
    }

    @Override
    public JPaintShapeAdapter getDraggedShape()
    {
        return this.draggedShape;
    }
    
    @Override
    public JPaintShapeAdapter getClickedShape()
    {
        return this.clickedShape;
    }

    @Override
    public ArrayList<JPaintShapeAdapter> getSelectedShapes()
    {
        return this.selectedShapes;
    }

    @Override
    public void setDraggedShape(JPaintShapeAdapter shape)
    {
        this.draggedShape = shape;
    }

    @Override
    public JPaintShapeAdapter getMouseDraggedDrawShape(Point mousePoint, Point mouseDragPoint, boolean flippedColors)
    {
        ShapeColor primaryColor = this.activePrimaryColor;
        ShapeColor secondaryColor = this.activeSecondaryColor;
        if (flippedColors)
        {
            secondaryColor = primaryColor;
            primaryColor = this.activeSecondaryColor;
        }
        JPaintShape shape = new JPaintShape(this.activeShapeType,
                                            primaryColor,
                                            secondaryColor,
                                            this.activeShapeShadingType,
                                            StartAndEndPointMode.DRAW);
        JPaintShapeAdapter adapter = new JPaintShapeAdapter(shape, Geometry.getDimensionsWithInvert(mousePoint, mouseDragPoint));
        return adapter;
    }

    @Override
    public JPaintShapeAdapter getMouseDraggedMoveShape(Point mousePoint, Point mouseDragPoint) {
        JPaintShape clickedShape = this.clickedShape.getJPaintShape();
        JPaintShape shape = new JPaintShape(clickedShape.getShapeType(),
                                            clickedShape.getPrimaryShapeColor(),
                                            clickedShape.getSecondaryShapeColor(),
                                            clickedShape.getShapeShadingType(),
                                            StartAndEndPointMode.MOVE);
        int deltaX = mousePoint.x - mouseDragPoint.x;
        int deltaY = mousePoint.y - mouseDragPoint.y;
        Dimensions dims = new Dimensions(new Point(this.clickedShape.getX() - deltaX, this.clickedShape.getY() - deltaY),
                                         new Point(this.clickedShape.getWidth(), this.clickedShape.getHeight()));
        return new JPaintShapeAdapter(shape, dims);
    }

    @Override
    public void resetDraggedShape()
    {
        this.draggedShape = null;
    }

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

    @Override
    public void setSelectedShape() {
        ArrayList<JPaintShapeAdapter>_shapes = new ArrayList<>();
        _shapes.add(this.clickedShape);
        this.selectedShapes = _shapes;
    }

    @Override 
    public void setSelectedShapesFromRectangle(Rectangle selection) {
        this.selectedShapes = this.getShapesInSelection(selection);
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

    private void setColorInSelectMode(CommandType type, ShapeColor color) {
        ICommand command = CommandFactory.Create(type, color, this.selectedShapes);
        commandHistory.add(command);
        command.execute();
        this.repaint();
    }

    private void setShadingTypeInSelectMode(ShapeShadingType shading)
    {
        ICommand changeShadingType = CommandFactory.Create(CommandType.ChangeShadingType, shading, this.selectedShapes);
        commandHistory.add(changeShadingType);
        changeShadingType.execute();
        this.repaint();
    }

    @Override
    public void setActiveShape() {
        activeShapeType = uiModule.getDialogResponse(dialogProvider.getChooseShapeDialog());
        displaySettingsShape.setShapeType(activeShapeType);
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActivePrimaryColor() {
        activePrimaryColor = uiModule.getDialogResponse(dialogProvider.getChoosePrimaryColorDialog());
        displaySettingsShape.setPrimaryColor(activePrimaryColor);
        if (this.activeStartAndEndPointMode == StartAndEndPointMode.SELECT)
        {
            setColorInSelectMode(CommandType.ChangePrimaryColor, activePrimaryColor);
            this.repaint();
        }
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveSecondaryColor() {
        activeSecondaryColor = uiModule.getDialogResponse(dialogProvider.getChooseSecondaryColorDialog());
        displaySettingsShape.setSecondaryColor(activeSecondaryColor);
        if (this.activeStartAndEndPointMode == StartAndEndPointMode.SELECT)
        {
            setColorInSelectMode(CommandType.ChangeSecondaryColor, activePrimaryColor);
            this.repaint();
        }
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveShadingType() {
        activeShapeShadingType = uiModule.getDialogResponse(dialogProvider.getChooseShadingTypeDialog());
        displaySettingsShape.setShapeShadingType(activeShapeShadingType);
        if (this.activeStartAndEndPointMode == StartAndEndPointMode.SELECT)
        {
            setShadingTypeInSelectMode(activeShapeShadingType);
            this.repaint();
        }
        uiModule.setShape(displaySettingsShape);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveStartAndEndPointMode() {
        activeStartAndEndPointMode = uiModule.getDialogResponse(dialogProvider.getChooseStartAndEndPointModeDialog());
        displaySettingsShape.setStartAndEndPointMode(activeStartAndEndPointMode);
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
