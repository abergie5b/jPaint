package model.persistence;

import model.StateModelAdapter;
import model.ShapeColor;
import model.ShapeShadingType;
import model.ShapeType;
import model.StartAndEndPointMode;
import model.dialogs.DialogProvider;
import model.interfaces.IApplicationState;
import model.interfaces.IDialogProvider;
import view.interfaces.IUiModule;

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

    public ApplicationState(IUiModule uiModule) 
    {
        this.uiModule = uiModule;
        this.dialogProvider = new DialogProvider(this);
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
        activeShapeType = ShapeType.ELLIPSE;
        activePrimaryColor = ShapeColor.BLUE;
        activeSecondaryColor = ShapeColor.GREEN;
        activeShapeShadingType = ShapeShadingType.FILLED_IN;
        activeStartAndEndPointMode = StartAndEndPointMode.DRAW;
    }
}
