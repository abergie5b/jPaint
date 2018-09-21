package model;

public class StateModel {

    public ShapeType shapeType;
    public ShapeColor primaryColor;
    public ShapeColor secondaryColor;
    public ShapeShadingType shapeShadingType;
    public StartAndEndPointMode startAndEndPointMode;

    public StateModel(ShapeType shapeType, 
                      ShapeColor primaryColor, 
                      ShapeColor secondaryColor, 
                      ShapeShadingType shapeShadingType, 
                      StartAndEndPointMode startAndEndPointMode) {
        this.shapeType = shapeType;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.shapeShadingType = shapeShadingType;
        this.startAndEndPointMode = startAndEndPointMode;
    }

}
