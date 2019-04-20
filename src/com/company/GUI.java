package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GUI extends JFrame {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int sizeWidth = 800;
    private static int sizeHeight = 600;
    private static int locationX = (screenSize.width - sizeWidth);
    private static int locationY = (screenSize.height - sizeHeight);

    private DefaultListModel<String> names = new DefaultListModel<>();
    private JList<String> listBox = new JList<>(names);

    GUI() {

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Options");
        menuBar.setBackground(Color.LIGHT_GRAY);
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuBar.add(fileMenu);

        final JList<String> list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(list);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(listScrollPane, BorderLayout.WEST);

        ActionListener updateButtonListener = new UpdateListAction(list);
        updateButtonListener.actionPerformed(
                new ActionEvent(list, ActionEvent.ACTION_PERFORMED, null)
        );

        final JButton updateLookAndFeelButton = new JButton("Update Theme");
        updateLookAndFeelButton.setToolTipText("Click to change your theme");
        JPanel btnPanel = new JPanel();
        btnPanel.add(updateLookAndFeelButton);
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnPanel);

        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        final JTextField textField = new JTextField();
        textField.setToolTipText("Enter task");
        JScrollPane tasksScrollPane = new JScrollPane(listBox);

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

        JButton addButton = new JButton("Add");
        addButton.setToolTipText("Click to add");

        final JLabel label = new JLabel();
        label.setForeground(Color.red);
        label.setFont(new Font("Consolas", Font.PLAIN, 16));


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern p = Pattern.compile("(([0-9]){0,}([a-zA-Z])([\\.]){0,})+");
                Matcher m = p.matcher(textField.getText());
                if (!m.matches()) {
                    label.setText("Вводите только цифры, буквы и символ \".\"");
                    textField.setText("");
                } else {
                    names.add(names.getSize(), textField.getText());
                    textField.setText("");
                }
            }
        });

        JButton removeButton = new JButton("Delete");
        removeButton.setToolTipText("Click to remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] arr = listBox.getSelectedIndices();
                for (int i1 : arr) {
                    names.remove(i1);
                }
            }
        });

        JPanel addRemovePanel = new JPanel();
        addRemovePanel.setLayout(new BoxLayout(addRemovePanel, BoxLayout.LINE_AXIS));
        btnPanel.add(addButton);
        btnPanel.add(Box.createHorizontalStrut(5));
        btnPanel.add(removeButton);

        topPanel.add(textField, BorderLayout.NORTH);
        topPanel.add(tasksScrollPane);

        JFrame frame = new JFrame("Taasky");
        frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        frame.setJMenuBar(menuBar);

        frame.add(label, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        updateLookAndFeelButton.addActionListener(
                new UpdateLookAndFeelAction(frame, list)
        );
    }
}