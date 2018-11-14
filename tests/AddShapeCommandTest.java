package tests;

import model.persistence.CommandFactory;
import model.persistence.ICommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import model.*;

import java.awt.Point;
import java.util.ArrayList;

class AddShapeCommandTest {

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
    void executeAddsShape() {
        ArrayList<JPaintShapeAdapter> shapes = new ArrayList<>();
        JPaintShapeAdapter shapeAdapter = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        JPaintShapeAdapter shapeAdapter2 = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        ICommand addShape = CommandFactory.CreateAddShapeCommand(shapes, shapeAdapter);
        addShape.execute();
        assertEquals(1, shapes.size());
        ICommand addShape2 = CommandFactory.CreateAddShapeCommand(shapes, shapeAdapter2);
        addShape2.execute();
        assertEquals(2, shapes.size());
    }

    @Test
    void undoRemovesShape() {
        ArrayList<JPaintShapeAdapter> shapes = new ArrayList<>();
        JPaintShapeAdapter shapeAdapter = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        JPaintShapeAdapter shapeAdapter2 = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        ICommand addShape = CommandFactory.CreateAddShapeCommand(shapes, shapeAdapter);
        addShape.execute();
        ICommand addShape2 = CommandFactory.CreateAddShapeCommand(shapes, shapeAdapter2);
        addShape2.execute();
        addShape.undo();
        assertEquals(1, shapes.size());
        addShape2.undo();
        assertEquals(0, shapes.size());
    }
}