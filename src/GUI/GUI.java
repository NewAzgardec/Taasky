package GUI;

import Themes.UpdateListAction;
import Themes.UpdateLookAndFeelAction;
import Threads.ClockThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI extends JFrame {

    public JLabel jLabelClock;
    ClockThread ct;

    private String FRAME = "Taasky";
    private String OPTIONS = "Options";
    private String ENGLISH = "Eng List";
    private String RUSSIAN = "Rus List";
    private String EXIT = "Exit";
    private String UPDATE = "Update Theme";
    private String ADD = "Add";
    private String DELETE = "Delete";
    private String LABEL = "<html><p align=center> You can only enter numbers, english letters, and symbol \".\"</p></html>";
    private String SYMBOLS = "<html><p align=center> The length of the string must be from 3 to 20 characters!</p></html>";
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
    private static int sizeWidth = 400;
    private static int sizeHeight = 400;
    private static int locationX = (screenSize.width - sizeWidth);
    private static int locationY = (screenSize.height - sizeHeight);

    private final JList<String> list;
    private DefaultListModel<String> names = new DefaultListModel<>();
    private JList<String> listBox = new JList<>(names);
    private int[] arr;


    public GUI() {

        jLabelClock = new JLabel();
        jLabelClock.setFont(new Font("Comin Sans MS", Font.BOLD, 16));
        add(jLabelClock);
        ct = new ClockThread(this);
        jLabelClock.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelClock.setVerticalAlignment(SwingConstants.CENTER);

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
        exitItem.addActionListener(e -> {
            //writeMethod();
            System.exit(0);
        });
        menuBar.add(fileMenu);

        list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //readMethod();
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
        DefaultListCellRenderer renderer =  (DefaultListCellRenderer)listBox.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        addButton = new JButton(ADD);
        addButton.setToolTipText(TIP_ADD);

        label = new JLabel();
        label.setForeground(Color.red);
        label.setFont(new Font("Comin Sans MS", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        addButton.addActionListener(e -> {
            pattern = Pattern.compile("(([0-9]){0,}([a-zA-Z]){0,}([ ])*([, ])*([\\.]){0,})+");
            matcher = pattern.matcher(textField.getText());

            if (textField.getText().length() <= 30&textField.getText().length()>=3) {
                if (!matcher.matches()) {
                    label.setText(LABEL);
                    textField.setText("");
                } else {
                    names.add(names.getSize(), textField.getText());
                    textField.setText("");
                    label.setText("");
                }
            } else {
                label.setText(SYMBOLS);
                textField.setText("");
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
        frame.add(jLabelClock, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        updateLookAndFeelButton.addActionListener(
                new UpdateLookAndFeelAction(frame, list)
        );
    }

//   public void writeMethod(){
//       FileOutputStream fileOS = null;
//       try {
//           fileOS = new FileOutputStream("q.xml");
//       } catch (FileNotFoundException e) {
//           e.printStackTrace();
//       }
//       XMLEncoder encoder=null;
//       encoder=new XMLEncoder(fileOS);
//       encoder.writeObject(list);
//       encoder.close();
//
//   }
//
//   public void readMethod(){
//       FileInputStream fileIS = null;
//       try {
//           File myFile = new File("q.xml");
//           try {
//               myFile.createNewFile();// if file already exists will do nothing
//           } catch (IOException e) {
//               e.printStackTrace();
//           }
//           FileOutputStream oFile = new FileOutputStream(myFile, false);
//           fileIS = new FileInputStream(myFile);
//       } catch (FileNotFoundException e) {
//           e.printStackTrace();
//       }
//       if(fileIS!=null) {
//           XMLDecoder decoder = new XMLDecoder(fileIS);
//           List decodedSettings = (List) decoder.readObject();
//           decoder.close();
//           try {
//               fileIS.close();
//           } catch (IOException e) {
//               e.printStackTrace();
//           }
//       }
//       else
//       {
//
//       }
//   }
}
