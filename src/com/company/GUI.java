package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GUI extends JFrame {

    private String FRAME = "Taasky";
    private String OPTIONS = "Options";
    private String ENGLISH = "Eng List";
    private String RUSSIAN = "Rus List";
    private String EXIT = "Exit";
    private String UPDATE = "Update Theme";
    private String ADD = "Add";
    private String DELETE = "Delete";
    private String LABEL = " You can only enter numbers, english letters, and symbol \".\"";
    private String TIP_UPDATE = "Click to change your theme";
    private String TIP_TEXTFIELD = "Enter task";
    private String TIP_ADD = "Click to add";
    private String TIP_DELETE = "Click to remove";

    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem englishList;
    private JMenuItem russianList;
    private JMenuItem exitItem;
    private JScrollPane listScrollPane;
    private JPanel topPanel;
    private JPanel btnPanel;
    private JPanel bottomPanel;
    private JScrollPane tasksScrollPane;
    private JButton removeButton;
    private JButton addButton;
    private JPanel addRemovePanel;
    private final JPanel panel;
    private final JTextField textField;
    private final JLabel label;

    private Pattern pattern;
    private Matcher matcher;

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int sizeWidth = 600;
    private static int sizeHeight = 400;
    private static int locationX = (screenSize.width - sizeWidth);
    private static int locationY = (screenSize.height - sizeHeight);

    private final JList<String> list;
    private DefaultListModel<String> names = new DefaultListModel<>();
    private JList<String> listBox = new JList<>(names);
    private int[] arr;


    public GUI() {

        menuBar = new JMenuBar();
        fileMenu = new JMenu(OPTIONS);
        menuBar.setBackground(Color.LIGHT_GRAY);
        englishList = new JMenuItem(ENGLISH);
        russianList = new JMenuItem(RUSSIAN);
        exitItem = new JMenuItem(EXIT);
        fileMenu.add(englishList);
        fileMenu.add(russianList);
        fileMenu.add(exitItem);

        russianList.addActionListener(e -> {

            frame.setVisible(false);
            new RusGUI();

        });
        exitItem.addActionListener(e -> System.exit(0));
        menuBar.add(fileMenu);

        list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listScrollPane = new JScrollPane(list);

        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(listScrollPane, BorderLayout.WEST);

        ActionListener updateButtonListener = new UpdateListAction(list);
        updateButtonListener.actionPerformed(
                new ActionEvent(list, ActionEvent.ACTION_PERFORMED, null)
        );

        final JButton updateLookAndFeelButton = new JButton(UPDATE);
        updateLookAndFeelButton.setToolTipText(TIP_UPDATE);
        btnPanel = new JPanel();
        btnPanel.add(updateLookAndFeelButton);
        bottomPanel = new JPanel();
        bottomPanel.add(btnPanel);

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        textField = new JTextField();
        textField.setToolTipText(TIP_TEXTFIELD);
        tasksScrollPane = new JScrollPane(listBox);

//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream("my.xml");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        XMLEncoder xmlEncoder = new XMLEncoder(out);
//        xmlEncoder.writeObject(listBox);
//        xmlEncoder.flush();
//        xmlEncoder.close();

        addButton = new JButton(ADD);
        addButton.setToolTipText(TIP_ADD);

        label = new JLabel();
        label.setForeground(Color.red);
        label.setFont(new Font("Consolas", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pattern = Pattern.compile("(([0-9]){0,}([a-zA-Z])([,])*([\\.]){0,})+");
                matcher = pattern.matcher(textField.getText());
                if (!matcher.matches()) {
                    label.setText(LABEL);
                    textField.setText("");
                } else {
                    names.add(names.getSize(), textField.getText());
                    textField.setText("");
                    label.setText("");
                }
            }
        });

        removeButton = new JButton(DELETE);
        removeButton.setToolTipText(TIP_DELETE);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                arr = listBox.getSelectedIndices();
                for (int i1 : arr) {
                    names.remove(i1);
                }
            }
        });

        addRemovePanel = new JPanel();
        addRemovePanel.setLayout(new BoxLayout(addRemovePanel, BoxLayout.LINE_AXIS));
        btnPanel.add(addButton);
        btnPanel.add(Box.createHorizontalStrut(5));
        btnPanel.add(removeButton);

        topPanel.add(textField, BorderLayout.NORTH);
        topPanel.add(tasksScrollPane);

        frame = new JFrame(FRAME);
        frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setJMenuBar(menuBar);
        frame.add(label, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        updateLookAndFeelButton.addActionListener(
                new UpdateLookAndFeelAction(frame, list)
        );
    }
}
