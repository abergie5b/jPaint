package controller;

import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;


public interface IMouseEventController 
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

