package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;*/


public class GUI extends JFrame {

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int sizeWidth = 800;
    public static int sizeHeight = 600;
    public static int locationX = (screenSize.width - sizeWidth);
    public static int locationY = (screenSize.height - sizeHeight);

    private DefaultListModel<String> names = new DefaultListModel<>();
    private JList listBox = new JList(names);
//    private String called = null;
//
//    private final String[] data = { "Тарзан","Человек-паук", "Аквамен", "Шерлок", "Сотня"};

    public GUI() {
       /* super("Фильмы и сериалы");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        final JPanel contents = new JPanel();
        contents.setSize(400,200);

        for (String string : data) {
            names.add(0, string);
        }


        final JTextField text= new JTextField();


        JButton add = new JButton("Добавить");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                names.add(names.getSize(), text.getText());

            }
        });

        JButton remove = new JButton("Просмотрено");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int []arr = listBox.getSelectedIndices();
                for(int i = 0; i < arr.length; ++i) {
                    names.remove(arr[i]);
                }

                *//*if (listBox.getSelectedIndex()!=-1) {
                    names.remove(listBox.getSelectedIndex() + 1);
                    System.out.println("Hi");
                }
*//*


            }
        } );






        JList<String> list2 = new JList<>(names);


        contents.add(text);
        contents.add(add);
        contents.add(remove);
        contents.add(new JScrollPane(list2));




        setContentPane(contents);

        setSize(400, 200);
        setVisible(true);
    }
}
*/


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

        //JButton updateListButton = new JButton("Update list");
        final JButton updateLookAndFeelButton = new JButton("Update Look&Feel");

        JPanel btnPannel = new JPanel();
        btnPannel.setLayout(new BoxLayout(btnPannel, BoxLayout.LINE_AXIS));
        //btnPannel.add(updateListButton);
        btnPannel.add(Box.createHorizontalStrut(5));
        btnPannel.add(updateLookAndFeelButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnPannel);

        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);






//        JPanel homePanel = new JPanel();
//        homePanel.setLayout(new BorderLayout());

        final JTextField textField = new JTextField();


        JScrollPane tasksScrollPane = new JScrollPane(listBox);


        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                names.add(names.getSize(), textField.getText());

            }
        });

        JButton removeButton = new JButton("Delete");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] arr = listBox.getSelectedIndices();
                for (int i = 0; i < arr.length; ++i) {
                    names.remove(arr[i]);
                }

            }
        });

        JPanel addRemovePannel = new JPanel();
        addRemovePannel.setLayout(new BoxLayout(addRemovePannel, BoxLayout.LINE_AXIS));
        btnPannel.add(addButton);
        btnPannel.add(Box.createHorizontalStrut(5));
        btnPannel.add(removeButton);

        topPanel.add(textField, BorderLayout.NORTH);
        topPanel.add(tasksScrollPane);
       // topPanel.add(addRemovePannel);





//        homePanel.add(textField, BorderLayout.CENTER);
//        homePanel.add(tasksScrollPane, BorderLayout.CENTER);
//        homePanel.add(addRemovePannel, BorderLayout.SOUTH);




        JFrame frame = new JFrame("Taasky");
        frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
        //frame.setMinimumSize(new Dimension(300, 200));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);


        //updateListButton.addActionListener(updateButtonListener);
        updateLookAndFeelButton.addActionListener(
                new UpdateLookAndFeelAction(frame, list)
        );
    }
}