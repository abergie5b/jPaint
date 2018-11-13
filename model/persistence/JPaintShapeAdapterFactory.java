package model.persistence;

import model.interfaces.IApplicationState;
import model.JPaintShapeAdapter;
import model.JPaintShape;
import model.*;

import java.awt.Point;
import java.awt.Rectangle;

public class JPaintShapeAdapterFactory {
    /**
     * @param appState
     * @param mousePoint
     * @param mouseDragPoint
     * @return
     */
    public static JPaintShapeAdapter Create(IApplicationState appState, Point mousePoint, Point mouseDragPoint) {
        switch (appState.getActiveStartAndEndPointMode())
        {
            case MOVE:
                return getMouseDraggedMoveShape(appState, mousePoint, mouseDragPoint);
            case SELECT:
                return getMouseDraggedSelectShape(appState, mousePoint, mouseDragPoint);
            default:
                throw new IllegalArgumentException("Invalid arguments found creating JPaintShapeAdapter");
        }
    }

    /**
     * @param appState
     * @param mousePoint
     * @param mouseDragPoint
     * @param flippedColors
     * @return
     */
    public static JPaintShapeAdapter Create(IApplicationState appState, Point mousePoint, Point mouseDragPoint, boolean flippedColors) {
        switch (appState.getActiveStartAndEndPointMode())
        {
            case DRAW:
                return getMouseDraggedDrawShape(appState, mousePoint, mouseDragPoint, flippedColors);
            default:
                throw new IllegalArgumentException("Invalid mode found creating JPaintShapeAdapter");
        }
    }

    private static JPaintShapeAdapter getMouseDraggedDrawShape(IApplicationState appState, Point mousePoint, Point mouseDragPoint, boolean flippedColors)
    {
        ShapeColor primaryColor = appState.getActivePrimaryColor();
        ShapeColor secondaryColor = appState.getActiveSecondaryColor();
        if (flippedColors)
        {
            secondaryColor = primaryColor;
            primaryColor = appState.getActiveSecondaryColor();
        }
        JPaintShape shape = new JPaintShape(appState.getActiveShapeType(),
                                            primaryColor,
                                            secondaryColor,
                                            appState.getActiveShapeShadingType(),
                                            StartAndEndPointMode.DRAW);
        JPaintShapeAdapter adapter = new JPaintShapeAdapter(shape, getDimensionsWithInvert(mousePoint, mouseDragPoint));
        return adapter;
    }

    private static JPaintShapeAdapter getMouseDraggedMoveShape(IApplicationState appState, Point mousePoint, Point mouseDragPoint) {
        JPaintShape clickedShape = appState.getClickedShape().getJPaintShape();
        JPaintShape shape = new JPaintShape(clickedShape.getShapeType(),
                                            clickedShape.getPrimaryShapeColor(),
                                            clickedShape.getSecondaryShapeColor(),
                                            clickedShape.getShapeShadingType(),
                                            StartAndEndPointMode.MOVE);
        int deltaX = mousePoint.x - mouseDragPoint.x;
        int deltaY = mousePoint.y - mouseDragPoint.y;
        Dimensions dims = new Dimensions(new Point(appState.getClickedShape().getX() - deltaX, appState.getClickedShape().getY() - deltaY),
                new Point(appState.getClickedShape().getWidth(), appState.getClickedShape().getHeight()));
        return new JPaintShapeAdapter(shape, dims);
    }

    private static JPaintShapeAdapter getMouseDraggedSelectShape(IApplicationState appState, Point mousePoint, Point mouseDragPoint) {
        Dimensions dims = getDimensionsWithInvert(mousePoint, mouseDragPoint);
        Rectangle selectionRectangle = new Rectangle(dims.xy.x, dims.xy.y, dims.wh.x, dims.wh.y);
        appState.setSelectedShapesFromRectangle(selectionRectangle);
        JPaintShape shape = new JPaintShape(ShapeType.RECTANGLE,
                                            ShapeColor.BLACK,
                                            ShapeColor.BLACK,
                                            ShapeShadingType.OUTLINE,
                                            StartAndEndPointMode.SELECT);
        JPaintShapeAdapter adapter = new JPaintShapeAdapter(shape, dims);
        return adapter;
    }

    private static Dimensions getDimensionsWithInvert(Point startXY, Point endXY)
    {
        int x = startXY.x;
        int y = startXY.y;
        int width = endXY.x - x;
        int height = endXY.y - y;
        if (width < 0)
        {
            x = endXY.x;
            width = Math.abs(width);
        }
        if (height < 0)
        {
            y = endXY.y;
            height = Math.abs(height);
        }
        return new Dimensions(new Point(x, y), new Point(width, height));
    }
}
