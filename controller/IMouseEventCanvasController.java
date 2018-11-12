package controller;

import java.awt.event.*;

public interface IMouseEventCanvasController 
{
    void mousePressedDraw(MouseEvent evt);
    void mousePressedMove(MouseEvent evt);
    void mousePressedSelect(MouseEvent evt);
    void mouseDraggedDraw(MouseEvent evt);
    void mouseDraggedMove(MouseEvent evt);
    void mouseDraggedSelect(MouseEvent evt);
    void mouseReleasedDraw(MouseEvent evt);
    void mouseReleasedMove(MouseEvent evt);
    void mouseReleasedSelect(MouseEvent evt);
}

