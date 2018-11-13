package model.persistence;

import view.gui.PaintCanvas;
import view.interfaces.IGuiWindow;
import view.gui.GuiWindow;
import model.*;

import java.awt.*;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationStateTest {
    private PaintCanvas paintCanvas;
    private IGuiWindow guiWindow;
    private ApplicationState appState;

    public ApplicationStateTest() {
        this.paintCanvas = new PaintCanvas();
        this.guiWindow = new GuiWindow(paintCanvas);
        this.appState = new ApplicationState(guiWindow);
    }

    private JPaintShapeAdapter createShapeAdapter(Point xy, Point wh) {
        JPaintShape shape = new JPaintShape(ShapeType.RECTANGLE,
                ShapeColor.LIGHT_GRAY,
                ShapeColor.GREEN,
                ShapeShadingType.OUTLINE_AND_FILLED_IN,
                StartAndEndPointMode.DRAW
        );
        Dimensions dims = new Dimensions(xy, wh);
        JPaintShapeAdapter shapeAdapter = new JPaintShapeAdapter(shape, dims);
        return shapeAdapter;
    }

    @Test
    void addShapeAppendsToShapes() {
        JPaintShapeAdapter shapeAdapter = this.createShapeAdapter(new Point(0, 0), new Point(50, 50));
        this.appState.addShape(shapeAdapter);
        assertEquals(1, this.appState.getShapes().size());
        JPaintShapeAdapter shapeAdapter2 = this.createShapeAdapter(new Point(0, 0), new Point(50, 50));
        this.appState.addShape(shapeAdapter2);
        assertEquals(2, this.appState.getShapes().size());
    }

    @Test
    void moveUpdatesShapeDimensions() {
        JPaintShapeAdapter from = this.createShapeAdapter(new Point(0, 0), new Point(50, 50));
        JPaintShapeAdapter to = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        this.appState.addShape(from);
        assertEquals(this.appState.getShapes().get(0), from);
        this.appState.move(from, to);
        JPaintShapeAdapter shape = this.appState.getShapes().get(0);
        assertEquals(shape, to);
        assertEquals(shape.getX(), 25);
        assertEquals(shape.getY(), 25);
    }

    @Test
    void deleteRemovesSelectedShapes() {
        JPaintShapeAdapter shapeAdapter = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        JPaintShapeAdapter shapeAdapter2 = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        JPaintShapeAdapter shapeAdapter3 = this.createShapeAdapter(new Point(75, 75), new Point(50, 50));
        this.appState.addShape(shapeAdapter);
        this.appState.addShape(shapeAdapter2);
        this.appState.addShape(shapeAdapter3);
        this.appState.setSelectedShapesFromRectangle(new Rectangle(0, 0, 50, 50));
        this.appState.delete();
        assertEquals(this.appState.getShapes().size(), 1);
        assertEquals(this.appState.getShapes().get(0), shapeAdapter3);
    }

    @Test
    void pasteAppendsCopiedShapes() {
        JPaintShapeAdapter shapeAdapter = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        JPaintShapeAdapter shapeAdapter2 = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        JPaintShapeAdapter shapeAdapter3 = this.createShapeAdapter(new Point(75, 75), new Point(50, 50));
        this.appState.addShape(shapeAdapter);
        this.appState.addShape(shapeAdapter2);
        this.appState.addShape(shapeAdapter3);
        this.appState.setSelectedShapesFromRectangle(new Rectangle(0, 0, 50, 50));
        this.appState.copy();
        this.appState.paste();
        assertEquals(this.appState.getShapes().size(), 5);
        JPaintShapeAdapter expected = this.createShapeAdapter(new Point(0, 0), new Point(shapeAdapter.getWidth(), shapeAdapter.getHeight()));
        JPaintShapeAdapter expected2 = this.createShapeAdapter(new Point(0, 0), new Point(shapeAdapter2.getWidth(), shapeAdapter2.getHeight()));
        assertEquals(this.appState.getShapes().get(3).getWidth(), expected.getWidth());
        assertEquals(this.appState.getShapes().get(3).getHeight(), expected.getHeight());
        assertEquals(this.appState.getShapes().get(3).getX(), expected.getX());
        assertEquals(this.appState.getShapes().get(3).getY(), expected.getY());
        assertEquals(this.appState.getShapes().get(4).getWidth(), expected2.getWidth());
        assertEquals(this.appState.getShapes().get(4).getHeight(), expected2.getHeight());
        assertEquals(this.appState.getShapes().get(4).getX(), expected2.getX());
        assertEquals(this.appState.getShapes().get(4).getY(), expected2.getY());
    }

    @Test
    void setSelectedShapesAssignsShapesInRectangle() {
        JPaintShapeAdapter shapeAdapter = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        JPaintShapeAdapter shapeAdapter2 = this.createShapeAdapter(new Point(25, 25), new Point(50, 50));
        JPaintShapeAdapter shapeAdapter3 = this.createShapeAdapter(new Point(75, 75), new Point(50, 50));
        this.appState.addShape(shapeAdapter);
        this.appState.addShape(shapeAdapter2);
        this.appState.addShape(shapeAdapter3);
        this.appState.setSelectedShapesFromRectangle(new Rectangle(0, 0, 50, 50));
        ArrayList<JPaintShapeAdapter> expected = new ArrayList<>();
        expected.add(shapeAdapter);
        expected.add(shapeAdapter2);
        assertEquals(this.appState.getSelectedShapes(), expected);
    }

    @Test
    void setClickedShapeReturnsShapeFromShapes() {
        JPaintShapeAdapter shapeAdapter = this.createShapeAdapter(new Point(25, 25), new Point(25, 25));
        JPaintShapeAdapter shapeAdapter2 = this.createShapeAdapter(new Point(50, 50), new Point(25, 25));
        JPaintShapeAdapter shapeAdapter3 = this.createShapeAdapter(new Point(75, 75), new Point(25, 25));
        this.appState.addShape(shapeAdapter);
        this.appState.addShape(shapeAdapter2);
        this.appState.addShape(shapeAdapter3);
        this.appState.setClickedShape(new Point(35, 35));
        assertEquals(this.appState.getClickedShape(), shapeAdapter);
        this.appState.setClickedShape(new Point(65, 65));
        assertEquals(this.appState.getClickedShape(), shapeAdapter2);
        this.appState.setClickedShape(new Point(85, 85));
        assertEquals(this.appState.getClickedShape(), shapeAdapter3);
    }

}