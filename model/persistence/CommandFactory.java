package model.persistence;

import model.JPaintShapeAdapter;
import java.util.ArrayList;
import model.ShapeColor;
import model.ShapeShadingType;

public class CommandFactory {
    /**
     * @param shapes
     * @param shape
     * @return
     */
    public static ICommand CreateAddShapeCommand(ArrayList<JPaintShapeAdapter> shapes, JPaintShapeAdapter shape) {
        return new AddShapeCommand(shapes, shape);
    }

    /**
     * @param type
     * @param color
     * @param selectedShapes
     * @return
     */
    public static ICommand CreateChangeColorCommand(CommandType type, ShapeColor color, ArrayList<JPaintShapeAdapter> selectedShapes) {
        ICommand command;
        switch (type)
        {
            case ChangePrimaryColor:
                command = new ChangePrimaryColorCommand(color, selectedShapes);
                break;
            case ChangeSecondaryColor:
                command = new ChangeSecondaryColorCommand(color, selectedShapes);
                break;
            default:
                throw new IllegalArgumentException("Invalid color command type: " + type);
        }
        return command;
    }

    /**
     * @param shading
     * @param selectedShapes
     * @return
     */
    public static ICommand CreateChangeShadingCommand(ShapeShadingType shading, ArrayList<JPaintShapeAdapter> selectedShapes) {
        return new ChangeShadingTypeCommand(shading, selectedShapes);
    }

    /**
     * @param shapes
     * @param from
     * @param to
     * @return
     */
    public static ICommand CreateMoveCommand(ArrayList<JPaintShapeAdapter> shapes, JPaintShapeAdapter from, JPaintShapeAdapter to) {
        return new MoveCommand(shapes, from, to);
    }

    /**
     * @param type
     * @param shapes
     * @param selectedShapes
     * @return
     */
    public static ICommand CreateSelectionCommand(CommandType type, ArrayList<JPaintShapeAdapter> shapes, ArrayList<JPaintShapeAdapter> selectedShapes) {
        ICommand command;
        switch (type)
        {
            case Delete:
                command = new DeleteCommand(shapes, selectedShapes);
                break;
            case Paste:
                command = new PasteCommand(shapes, selectedShapes);
                break;
            default:
                throw new IllegalArgumentException("Invalid selection shape command type" + type);
        }
        return command;
    }
}
