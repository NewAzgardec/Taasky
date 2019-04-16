package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;

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
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField.getText() != null && !textField.getText().equals("")) {
                    names.add(names.getSize(), textField.getText());
                    textField.setText("");
                }
            }
        });

        JButton removeButton = new JButton("Delete");
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

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        updateLookAndFeelButton.addActionListener(
                new UpdateLookAndFeelAction(frame, list)
        );
    }
}