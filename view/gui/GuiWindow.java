package view.gui;

import model.*;

import view.interfaces.IGuiWindow;
import view.EventName;
import view.gui.PaintCanvas;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.awt.geom.*;
import java.awt.*;

public class GuiWindow extends JFrame implements IGuiWindow 
{
    private final int defaultWidth = 1080;
    private final int defaultHeight = 720;
    private final String defaultTitle = "~JPaint~";
    private final Insets defaultButtonDimensions = new Insets(5, 8, 5, 8);
    private final Map<EventName, JButton> eventButtons = new HashMap<>();
    private JLabel label1 = new JLabel("");
    private JLabel label2 = new JLabel("");
    private JLabel label3 = new JLabel("");
    private JLabel label4 = new JLabel("");
    private JLabel label5 = new JLabel("");
    private PaintCanvas canvas;
    private ShapeAdapter stateModel;

    public GuiWindow(PaintCanvas canvas)
    {
        this.canvas = canvas;
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(defaultTitle);
        setSize(defaultWidth, defaultHeight);
        JPanel window = createWindow();
        window.add(canvas, BorderLayout.CENTER);
		validate();
    }

    @Override
	public JButton getButton(EventName eventName) {
		if(!eventButtons.containsKey(eventName))
			throw new NoSuchElementException("No button exists for action " + eventName.toString());
		
		return eventButtons.get(eventName);
	}

    @Override
    public void setShape(ShapeAdapter stateModel) {
        this.stateModel = stateModel;
    }

    @Override
    public PaintCanvas getCanvas() {
        return this.canvas;
    }

    @Override
    public void setStatusMenu() {
        label1.setText("(" + "SHAPE: " + stateModel.getShapeName() + ")");
        label2.setText("(" + "COLORS:  " + stateModel.getPrimaryColorName() + ",");
        label3.setText(stateModel.getSecondaryColorName() + ")");
        label4.setText("(" + "SHADING: " + stateModel.shapeShadingType.name() + ")");
        label5.setText("(" + "MODE: " + stateModel.startAndEndPointMode.name() + ")");
    }

    private JPanel createStatusMenu() {
        JPanel statusPanel = createStatusPanel();
        statusPanel.add(label1);
        statusPanel.add(label2);
        statusPanel.add(label3);
        statusPanel.add(label4);
        statusPanel.add(label5);
        return statusPanel;
    }

	private JPanel createWindow() {
		JPanel contentPane = createBackgroundPanel();
        JPanel statusPanel = createStatusMenu();
        JPanel buttonPanel = createOptionsMenu();
        contentPane.add(buttonPanel, BorderLayout.WEST);
        contentPane.add(statusPanel, BorderLayout.SOUTH);
        return contentPane;
	}

    private JPanel createOptionsMenu() {
        JPanel buttonPanel = createButtonPanel();

        for(EventName eventName : EventName.values()){
            addButtonToPanel(eventName, buttonPanel);
        }

        return buttonPanel;
    }

	private void addButtonToPanel(EventName eventName, JPanel panel) {
		JButton newButton = createButton(eventName);
        eventButtons.put(eventName, newButton);
        panel.add(newButton);
	}

	private JButton createButton(EventName eventName) {
		JButton newButton = new JButton(eventName.toString());
		newButton.setForeground(Color.BLACK);
		newButton.setBackground(Color.WHITE);
        newButton.setBorder(createButtonBorder());
		return newButton;
	}

	private Border createButtonBorder() {
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(defaultButtonDimensions);
    	return new CompoundBorder(line, margin);
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(20, 1));
        panel.setBackground(Color.lightGray);
		return panel;
	}

	private JPanel createStatusPanel() {
		JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        panel.setBackground(Color.lightGray);
		return panel;
	}

    private JPanel createBackgroundPanel() {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        return contentPane;
    }
}
