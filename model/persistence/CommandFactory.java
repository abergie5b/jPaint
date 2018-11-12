package model.persistence;

import model.JPaintShapeAdapter;
import java.util.ArrayList;
import model.ShapeColor;
import model.ShapeShadingType;

public class CommandFactory {
    public static ICommand Create(CommandType name, ArrayList<JPaintShapeAdapter> shapes, JPaintShapeAdapter shape) {
        ICommand command;
        switch (name)
        {
            case AddShape:
                command = new AddShapeCommand(shapes, shape);
                break;
            default:
                throw new IllegalArgumentException("Invalid shape command type: " + name);
        }
        return command;
    }

    public static ICommand Create(CommandType name, ShapeColor color, ArrayList<JPaintShapeAdapter> selectedShapes) {
        ICommand command;
        switch (name)
        {
            case ChangePrimaryColor:
                command = new ChangePrimaryColorCommand(color, selectedShapes);
                break;
            case ChangeSecondaryColor:
                command = new ChangeSecondaryColorCommand(color, selectedShapes);
                break;
            default:
                throw new IllegalArgumentException("Invalid color command type: " + name);
        }
        return command;
    }

    public static ICommand Create(CommandType name, ShapeShadingType shading, ArrayList<JPaintShapeAdapter> selectedShapes) {
        ICommand command;
        switch (name)
        {
            case ChangeShadingType:
                command = new ChangeShadingTypeCommand(shading, selectedShapes);
                break;
            default:
                throw new IllegalArgumentException("Invalid shape shading command type: " + name);
        }
        return command;
    }

    public static ICommand Create(CommandType name, ArrayList<JPaintShapeAdapter> shapes, JPaintShapeAdapter from, JPaintShapeAdapter to) {
        ICommand command;
        switch (name)
        {
            case Move:
                command = new MoveCommand(shapes, from, to);
                break;
            default:
                throw new IllegalArgumentException("Invalid move command type: " + name);
        }
        return command;
    }

    public static ICommand Create(CommandType name, ArrayList<JPaintShapeAdapter> shapes, ArrayList<JPaintShapeAdapter> selectedShapes) {
        ICommand command;
        switch (name)
        {
            case Delete:
                command = new DeleteCommand(shapes, selectedShapes);
                break;
            case Paste:
                command = new PasteCommand(shapes, selectedShapes);
                break;
            default:
                throw new IllegalArgumentException("Invalid selection shape command type" + name);
        }
        return command;
    }
}
