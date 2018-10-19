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
    private ArrayList<ShapeAdapter> shapes;
    private ArrayList<ShapeAdapter> shapeHistory;

    public ApplicationState(IGuiWindow uiModule) 
    {
        this.uiModule = uiModule;
        this.canvas = uiModule.getCanvas();
        this.dialogProvider = new DialogProvider(this);
        this.shapes = new ArrayList<ShapeAdapter>();
        this.shapeHistory = new ArrayList<ShapeAdapter>();
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

    private int getNumberOfShapes() 
    {
        return this.shapes.size();
    }

    private int getNumberOfShapeHistory() 
    {
        return this.shapeHistory.size();
    }

    public void redo() 
    {
        // TODO implement Command Pattern
        if (getNumberOfShapeHistory() > 0) 
        {
            ShapeAdapter s = this.shapeHistory.get(getNumberOfShapeHistory() - 1);
            this.shapeHistory.remove(getNumberOfShapeHistory() - 1);
            this.shapes.add(s);
        }
        this.repaint();
    }
    public void undo() 
    {
        // TODO implement Command Pattern
        if (getNumberOfShapes() > 0) 
        {
            ShapeAdapter s = this.shapes.get(getNumberOfShapes() - 1);
            this.shapes.remove(this.getNumberOfShapes() - 1);
            this.shapeHistory.add(s);
        }
        this.repaint();
    }

    public void delete()
    {
        for (ShapeAdapter ss: this.selectedShapes)
        {
            this.removeShapeFromBuffer(ss);
        }
        this.repaint();
    }

    @Override
    public void repaint()
    {
        this.canvas.repaintCanvas(shapes);
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

    @Override
    public void setCanvasShapes()
    {
        this.canvas.setShapes(this.shapes);
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
                System.out.println("Selected " + s.shape);
            }
        }
        return selectedShapes;
    }

    @Override
    public void removeShapeFromBuffer(ShapeAdapter shape) 
    {
        for (int x=0; x<this.shapes.size(); x++)
        {
            ShapeAdapter s = this.shapes.get(x);
            if (shape.equals(s))
            {
                System.out.println("Removing shape: " + s);
                this.shapes.remove(x);
            }
        }
    }

    @Override
    public void addShapeAttribute(ShapeAdapter _shape) 
    {
        this.shapes.add(_shape);
    }


    @Override
    public void setMouseDraggedShape(ShapeAdapter _shape) 
    {
        this.canvas.setMouseDraggedShape(_shape);
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
            this.addShapeAttribute(newShape);
        }
        repaint();
    }

    @Override 
    public void setSelectedShapes(Rectangle selection) {
        this.selectedShapes = this.getShapesinSelection(selection);
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
