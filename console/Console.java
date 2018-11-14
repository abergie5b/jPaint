package console;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.awt.Point;

import java.lang.reflect.*;

import model.persistence.JPaintShapeAdapterFactory;
import model.JPaintShapeAdapter;
import model.*;
import model.interfaces.IApplicationState;

public class Console {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private IApplicationState appState;
    private HashMap<ConsoleCommand, Class[]> consoleCommands;

    public Console(IApplicationState appState) {
        this.appState = appState;
        this.consoleCommands = new HashMap<>();
        this.setConsoleCommands();
        prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("JPaint Console");
        mainFrame.setSize(700,300);
        mainFrame.setLayout(new GridLayout(3, 3));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setSize(500,100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    public void showTextArea() {
        headerLabel.setText("JPaint Console");
        JLabel commentLabel = new JLabel("", JLabel.RIGHT);

        final JTextArea commentTextArea = new JTextArea("",5,20);

        JScrollPane scrollPane = new JScrollPane(commentTextArea);
        JButton showButton = new JButton("Execute");

        showButton.addActionListener(e -> this.execute(commentTextArea.getText()));
        commentTextArea.setText("");
        controlPanel.add(commentLabel);
        controlPanel.add(scrollPane);
        controlPanel.add(showButton);
        mainFrame.setVisible(true);
    }

    private void setConsoleCommands() {
        consoleCommands.put(ConsoleCommand.ADDSHAPE, new Class[] {JPaintShapeAdapter.class});
        consoleCommands.put(ConsoleCommand.UNDO, new Class[] {});
        consoleCommands.put(ConsoleCommand.REDO, new Class[] {});
        consoleCommands.put(ConsoleCommand.GETSHAPES, new Class[] {});
        consoleCommands.put(ConsoleCommand.SETACTIVEPRIMARYCOLOR, new Class[] {ShapeColor.class});
    }

    private Class[] getClassType(ConsoleCommand command) {
        return consoleCommands.get(command);
    }

    private ShapeColor getColorType (String name) {
        ShapeColor color = null;
        Field[] fields = ShapeColor.class.getDeclaredFields();
        for (Field f: fields)
        {
            if (name.equals(f.getName()) && Modifier.isStatic(f.getModifiers()))
            {
                try
                {
                    color = (ShapeColor)ShapeColor.class.getDeclaredField(name).get(null);
                }
                catch (NoSuchFieldException e) {
                    System.out.println("Invalid Color found trying to convert from string\n" + e);
                }
                catch (IllegalAccessException e) {
                    System.out.println("Unable to get Color attribute trying to convert from string\n" + e);
                }
            }
        }
        return color;
    }

    private JPaintShapeAdapter createJPaintShapeAdapter (Point xy, Point wh) {
        JPaintShapeAdapter adapter = JPaintShapeAdapterFactory.Create(this.appState,
                                                                      xy,
                                                                      wh,
                                                                      false);
        return adapter;
    }

    private String addShapeConsoleCommand(Method method, ArrayList<String> args)
    {
        Point xy = new Point(Integer.parseInt(args.get(1)), Integer.parseInt(args.get(2)));
        Point wh = new Point(Integer.parseInt(args.get(3)), Integer.parseInt(args.get(4)));
        try {
            method.invoke(this.appState, this.createJPaintShapeAdapter(xy, wh));
        }
        catch (InvocationTargetException e) {
            System.out.println("Invalid method called on appState trying to execute command\n" + e);
        }
        catch (IllegalAccessException e) {
            System.out.println("Unable to access appState trying to execute command\n" + e);
        }
        return "" + this.appState.getShapes().get(this.appState.getShapes().size()-1);
    }

    private String setPrimaryColorCommand(Method method, String color) {
        ShapeColor shapeColor = this.getColorType(color);
        try {
            method.invoke(this.appState, shapeColor);
        }
        catch (InvocationTargetException e) {
            System.out.println("Invalid method called on appState trying to execute command\n" + e);
        }
        catch (IllegalAccessException e) {
            System.out.println("Unable to access appState trying to execute command\n" + e);
        }
        return "" + this.appState.getActivePrimaryColor();
    }

    private void execute(String text) {
        this.statusLabel.setText(text);
        ArrayList<String> args = new ArrayList<> (Arrays.asList(text.split(" ")));
        String name = args.get(0);

        Field[] fields = ConsoleCommand.class.getDeclaredFields();
        ConsoleCommand command = null;
        for (Field f: fields)
        {
            if (name.toUpperCase().equals(f.getName().toUpperCase()) && Modifier.isStatic(f.getModifiers()))
            {
                try
                {
                    command = (ConsoleCommand) ConsoleCommand.class.getDeclaredField(name.toUpperCase()).get(null);
                }
                catch (NoSuchFieldException e) {
                    System.out.println("Invalid Color found trying to convert from string\n" + e);
                }
                catch (IllegalAccessException e) {
                    System.out.println("Unable to get Color attribute trying to convert from string\n" + e);
                }
            }
        }
        if (command == null)
        {
            throw new IllegalArgumentException("Could not find ConsoleCommand");
        }
        else
        {
            System.out.println("Found console command: " + command);
        }

        Class[] classArgs = this.getClassType(command);
        Method method = null;
        try {
            switch (command)
            {
                case GETSHAPES:
                    method = this.appState.getClass().getMethod(name);
                    ArrayList<JPaintShapeAdapter> shapes = (ArrayList<JPaintShapeAdapter>) method.invoke(this.appState);
                    String output = "";
                    for (JPaintShapeAdapter s: shapes)
                    {
                        output += s + "\n";
                    }
                    this.statusLabel.setText(output);
                    break;
                case UNDO:
                    method = this.appState.getClass().getMethod(name);
                    method.invoke(this.appState);
                    break;
                case REDO:
                    method = this.appState.getClass().getMethod(name);
                    method.invoke(this.appState);
                    break;
                case COPY:
                    method = this.appState.getClass().getMethod(name);
                    method.invoke(this.appState);
                    break;
                case DELETE:
                    method = this.appState.getClass().getMethod(name);
                    method.invoke(this.appState);
                    break;
                case PASTE:
                    method = this.appState.getClass().getMethod(name);
                    method.invoke(this.appState);
                    break;
                case ADDSHAPE:
                    method = this.appState.getClass().getMethod(name, classArgs[0]);
                    this.addShapeConsoleCommand(method, args);
                    break;
                case SETACTIVEPRIMARYCOLOR:
                    String color = args.get(1);
                    method = this.appState.getClass().getMethod(name, classArgs[0]);
                    this.setPrimaryColorCommand(method, color.toUpperCase());
                    break;
                default:
                    throw new IllegalArgumentException("Could not find ConsoleCommand type");
            }
        }
        catch (NoSuchMethodException e) {
            System.out.println("No method found trying to execute command\n" + e);
        }
        catch (InvocationTargetException e) {
            System.out.println("Invalid method called on appState trying to execute command\n" + e);
        }
        catch (IllegalAccessException e) {
            System.out.println("Unable to access appState trying to execute command\n" + e);
        }
        this.appState.repaint();
    }
}
