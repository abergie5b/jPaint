package model.persistence;

import model.JPaintShapeAdapter;
import java.util.ArrayList;
import model.ShapeColor;
import model.ShapeShadingType;

class CommandFactory {
    /**
     * @param type
     * @param shapes
     * @param shape
     * @return
     */
    static ICommand CreateAddShapeCommand(CommandType type, ArrayList<JPaintShapeAdapter> shapes, JPaintShapeAdapter shape) {
        ICommand command;
        switch (type)
        {
            case AddShape:
                command = new AddShapeCommand(shapes, shape);
                break;
            default:
                throw new IllegalArgumentException("Invalid shape command type: " + type);
        }
        return command;
    }

    /**
     * @param type
     * @param color
     * @param selectedShapes
     * @return
     */
    static ICommand CreateChangeColorCommand(CommandType type, ShapeColor color, ArrayList<JPaintShapeAdapter> selectedShapes) {
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
     * @param type
     * @param shading
     * @param selectedShapes
     * @return
     */
    static ICommand CreateChangeShadingCommand(CommandType type, ShapeShadingType shading, ArrayList<JPaintShapeAdapter> selectedShapes) {
        ICommand command;
        switch (type)
        {
            case ChangeShadingType:
                command = new ChangeShadingTypeCommand(shading, selectedShapes);
                break;
            default:
                throw new IllegalArgumentException("Invalid shape shading command type: " + type);
        }
        return command;
    }

    /**
     * @param type
     * @param shapes
     * @param from
     * @param to
     * @return
     */
    static ICommand CreateMoveCommand(CommandType type, ArrayList<JPaintShapeAdapter> shapes, JPaintShapeAdapter from, JPaintShapeAdapter to) {
        ICommand command;
        switch (type)
        {
            case Move:
                command = new MoveCommand(shapes, from, to);
                break;
            default:
                throw new IllegalArgumentException("Invalid move command type: " + type);
        }
        return command;
    }

    /**
     * @param type
     * @param shapes
     * @param selectedShapes
     * @return
     */
    static ICommand CreateSelectionCommand(CommandType type, ArrayList<JPaintShapeAdapter> shapes, ArrayList<JPaintShapeAdapter> selectedShapes) {
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
