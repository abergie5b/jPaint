package model.persistence;

import model.*;
import model.dialogs.DialogProvider;
import model.interfaces.IApplicationState;
import model.interfaces.IDialogProvider;
import view.interfaces.IUiModule;
import view.gui.PaintCanvas;

import java.util.ArrayList;
import java.io.Serializable;

public class ApplicationState implements IApplicationState, Serializable 
{
    private static final long serialVersionUID = -5545483996576839007L;
    private final IUiModule uiModule;
    private final IDialogProvider dialogProvider;

    private ShapeType activeShapeType;
    private ShapeColor activePrimaryColor;
    private ShapeColor activeSecondaryColor;
    private ShapeShadingType activeShapeShadingType;
    private StartAndEndPointMode activeStartAndEndPointMode;
    private StateModelAdapter stateModel;
    private ArrayList<StateModelAdapter> selectedShapes;

    public ApplicationState(IUiModule uiModule) 
    {
        this.uiModule = uiModule;
        this.dialogProvider = new DialogProvider(this);
        this.selectedShapes = new ArrayList<StateModelAdapter>();
        setDefaults();
        stateModel = new StateModelAdapter(activeShapeType, 
                                           activePrimaryColor,
                                           activeSecondaryColor, 
                                           activeShapeShadingType,
                                           activeStartAndEndPointMode
                                           );
        uiModule.setStateModel(stateModel);
        uiModule.setStatusMenu();
    }

    @Override
    public void undo() {
        uiModule.getCanvas().undo();
    }

    @Override
    public void redo() {
        uiModule.getCanvas().redo();
    }

    @Override
    public void delete() {
        uiModule.getCanvas().deleteShapes(selectedShapes);
    }

    @Override
    public void copy() {
    }

    @Override
    public void paste() {
        PaintCanvas canvas = uiModule.getCanvas();
        for (StateModelAdapter s: selectedShapes)
        {
            StateModelAdapter newShape = new StateModelAdapter(s.shapeType,
                                                               s.primaryShapeColor,
                                                               s.secondaryShapeColor,
                                                               s.shapeShadingType,
                                                               s.startAndEndPointMode
                                                               );
            newShape.setShape(0, 0, s.getWidth(), s.getHeight());
            canvas.addShapeAttribute(newShape);
        }
        canvas.repaint();
    }

    @Override 
    public void setSelectedShapes(ArrayList<StateModelAdapter> selection) {
        this.selectedShapes = selection;
    }

    @Override
    public void setActiveShape() {
        activeShapeType = uiModule.getDialogResponse(dialogProvider.getChooseShapeDialog());
        stateModel.setShapeType(activeShapeType);
        uiModule.setStateModel(stateModel);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActivePrimaryColor() {
        activePrimaryColor = uiModule.getDialogResponse(dialogProvider.getChoosePrimaryColorDialog());
        stateModel.updatePrimaryColor(activePrimaryColor);
        uiModule.setStateModel(stateModel);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveSecondaryColor() {
        activeSecondaryColor = uiModule.getDialogResponse(dialogProvider.getChooseSecondaryColorDialog());
        stateModel.updateSecondaryColor(activeSecondaryColor);
        uiModule.setStateModel(stateModel);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveShadingType() {
        activeShapeShadingType = uiModule.getDialogResponse(dialogProvider.getChooseShadingTypeDialog());
        stateModel.shapeShadingType = activeShapeShadingType;
        uiModule.setStateModel(stateModel);
        uiModule.setStatusMenu();
    }

    @Override
    public void setActiveStartAndEndPointMode() {
        activeStartAndEndPointMode = uiModule.getDialogResponse(dialogProvider.getChooseStartAndEndPointModeDialog());
        stateModel.startAndEndPointMode = activeStartAndEndPointMode;
        uiModule.setStateModel(stateModel);
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
