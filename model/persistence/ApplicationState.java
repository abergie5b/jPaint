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

    private ShapeAdapter shapeAdapter;
    private ShapeType activeShapeType;
    private ShapeColor activePrimaryColor;
    private ShapeColor activeSecondaryColor;
    private ShapeShadingType activeShapeShadingType;
    private StartAndEndPointMode activeStartAndEndPointMode;

    public ShapeAdapter clickedShape;
    public ShapeAdapter draggedShape;
    public ArrayList<ShapeAdapter> selectedShapes;

    public ApplicationState(IGuiWindow uiModule) 
    {
        this.uiModule = uiModule;
        this.canvas = uiModule.getCanvas();
        this.dialogProvider = new DialogProvider(this);
        this.selectedShapes = new ArrayList<ShapeAdapter>();
        this.clickedShape = null;
        this.draggedShape = null;
        this.selectedShapes = null;
        setDefaults();
        shapeAdapter = new ShapeAdapter(activeShapeType, 
                                        activePrimaryColor,
                                        activeSecondaryColor, 
                                        activeShapeShadingType,
                                        activeStartAndEndPointMode
        );
        uiModule.setShape(shapeAdapter);
        uiModule.setStatusMenu();
    }

    public ShapeAdapter getDraggedShape()
    {
        return this.draggedShape;
    }
    public ShapeAdapter getClickedShape()
    {
        return this.clickedShape;
    }
    public ArrayList<ShapeAdapter> getSelectedShapes()
    {
        return this.selectedShapes;
    }

    public void setDraggedShape(ShapeAdapter shape)
    {
        this.draggedShape = shape;
    }

    public void resetDraggedShape()
    {
        this.draggedShape = null;
    }

    public void setClickedShape(Point point)
    {
        // TODO move this to ApplicationState
        this.clickedShape = this.getShapeFromBuffer(point);
    }
    
    public void repaint() 
    {
        this.canvas.repaint();
    }

    public ArrayList<ShapeAdapter> getShapesinSelection(Rectangle selection)
    {
        ArrayList<ShapeAdapter> selectedShapes = new ArrayList<ShapeAdapter>();
        for (ShapeAdapter s: this.canvas.shapes) 
        {
            if (s.shape.intersects(selection))
            {
                selectedShapes.add(s);
                System.out.println("Selected " + s.shape);
            }
        }
        return selectedShapes;
    }

    public void removeShapeFromBuffer(ShapeAdapter shape) 
    {
        for (int x=0; x<this.canvas.shapes.size(); x++)
        {
            ShapeAdapter s = this.canvas.shapes.get(x);
            if (shape.equals(s))
            {
                System.out.println("Removing shape: " + s);
                this.canvas.shapes.remove(x);
            }
        }
    }


    public void addShapeAttribute(ShapeAdapter _shape) 
    {
        this.canvas.shapes.add(_shape);
    }


    public void setTempShape(ShapeAdapter _shape) 
    {
        this.canvas.setTempShape(_shape);
    }


    public ShapeAdapter getShapeFromBuffer(Point point) 
    {
        ShapeAdapter _shape = null;
        for (ShapeAdapter s: this.canvas.shapes)
        {
            if (s.shape.contains(point))
            {
                _shape = s;
            }
        }
        return _shape;
    }

    @Override
    public void undo() {
        this.canvas.undo();
    }

    @Override
    public void redo() {
        this.canvas.redo();
    }

    @Override
    public void delete() {
        this.canvas.deleteShapes(selectedShapes);
    }

    @Override
    public void copy() {
    }

    @Override
    public void paste() {
        for (ShapeAdapter s: selectedShapes)
        {
            ShapeAdapter newShape = new ShapeAdapter(s.shapeType,
                                                     s.primaryShapeColor,
                                                     s.secondaryShapeColor,
                                                     s.shapeShadingType,
                                                     s.startAndEndPointMode
            );
            newShape.setShape(new Dimensions(new Point(0, 0), new Point(s.getWidth(), s.getHeight())));
            this.canvas.addShapeAttribute(newShape);
        }
        repaint();
    }

    @Override 
    public void setSelectedShapes(ArrayList<ShapeAdapter> selection) {
        this.selectedShapes = selection;
    }

    @Override
    public void setActiveShape() {
        activeShapeType = uiModule.getDialogResponse(dialogProvider.getChooseShapeDialog());
        shapeAdapter.setShapeType(activeShapeType);
        uiModule.setShape(shapeAdapter);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActivePrimaryColor() {
        activePrimaryColor = uiModule.getDialogResponse(dialogProvider.getChoosePrimaryColorDialog());
        shapeAdapter.updatePrimaryColor(activePrimaryColor);
        uiModule.setShape(shapeAdapter);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveSecondaryColor() {
        activeSecondaryColor = uiModule.getDialogResponse(dialogProvider.getChooseSecondaryColorDialog());
        shapeAdapter.updateSecondaryColor(activeSecondaryColor);
        uiModule.setShape(shapeAdapter);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveShadingType() {
        activeShapeShadingType = uiModule.getDialogResponse(dialogProvider.getChooseShadingTypeDialog());
        shapeAdapter.shapeShadingType = activeShapeShadingType;
        uiModule.setShape(shapeAdapter);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveStartAndEndPointMode() {
        activeStartAndEndPointMode = uiModule.getDialogResponse(dialogProvider.getChooseStartAndEndPointModeDialog());
        shapeAdapter.startAndEndPointMode = activeStartAndEndPointMode;
        uiModule.setShape(shapeAdapter);
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
