package tests;

import model.persistence.CommandFactory;
import model.persistence.CommandType;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import model.*;

import static org.junit.jupiter.api.Assertions.*;
import model.persistence.CommandType;

class CommandFactoryTest {

    private JPaintShapeAdapter createShapeAdapter(Point xy, Point wh) {
        JPaintShape shape = new JPaintShape(ShapeType.RECTANGLE,
                                            ShapeColor.LIGHT_GRAY,
                                            ShapeColor.GREEN,
                                            ShapeShadingType.OUTLINE_AND_FILLED_IN,
                                            StartAndEndPointMode.DRAW
        );
        Dimensions dims = new Dimensions(xy, wh);
        return new JPaintShapeAdapter(shape, dims);
    }

    @Test
    void createChangeColorCommandThrowsIllegalArgumentExceptionForInvalidCommandType() {
        ArrayList<JPaintShapeAdapter> shapes = new ArrayList<>();
        JPaintShapeAdapter shapeAdapter = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        JPaintShapeAdapter shapeAdapter2 = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        shapes.add(shapeAdapter);
        shapes.add(shapeAdapter2);
        CommandFactory commandFactory = new CommandFactory();
        assertThrows(IllegalArgumentException.class, () -> commandFactory.CreateChangeColorCommand(CommandType.ChangeShadingType, ShapeColor.BLUE, shapes));
    }


    @Test
    void createSelectionCommandThrowsIllegalArgumentExceptionForInvalidCommandType() {
        ArrayList<JPaintShapeAdapter> shapes = new ArrayList<>();
        JPaintShapeAdapter shapeAdapter = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        JPaintShapeAdapter shapeAdapter2 = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        shapes.add(shapeAdapter);
        shapes.add(shapeAdapter2);
        ArrayList<JPaintShapeAdapter> shapes2 = new ArrayList<>();
        JPaintShapeAdapter shapeAdapter3 = this.createShapeAdapter(new Point(50, 50), new Point(50, 50));
        shapes2.add(shapeAdapter3);
        CommandFactory commandFactory = new CommandFactory();
        assertThrows(IllegalArgumentException.class, () -> commandFactory.CreateSelectionCommand(CommandType.ChangeShadingType, shapes, shapes2));
    }
}