package controller;

import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;


public interface IMouseEventController 
{
    void mousePressedDraw(int startX, int startY);
    void mousePressedMove(Point point);
    void mousePressedSelect(int startX, int startY);
    void mouseDraggedDraw(int endX, int endY);
    void mouseDraggedMove(int endX, int endY);
    void mouseDraggedSelect(int endX, int endY);
    void mouseReleasedDraw(int endX, int endY);
    void mouseReleasedMove(int endX, int endY);
    void mouseReleasedSelect(int endX, int endY);
}

