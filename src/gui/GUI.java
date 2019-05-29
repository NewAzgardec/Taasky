package gui;

import localization.MyLocale;
import other.*;
import themes.UpdateListAction;
import themes.UpdateLookAndFeelAction;
import threads.ClockThread;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI extends JFrame {

    private static final String HISTORY = ("history.txt");
    private static final String FAVORITE = ("favorite.txt");

    private static final String IMAGE_PATH = ("C:\\Users\\Masha\\IntelliJIDEAProjects\\AipLaba\\src\\config\\icon1.png");
    private static final String INPUT_S = ("C:\\Users\\Masha\\IntelliJIDEAProjects\\AipLaba\\src\\config\\langs.properties");

    private static String defaultLang;
    private static Set<String> langSet;

    private String LIST_OF_SYMBOLS = ("(([0-9]){0,}([a-zа-яA-ZА-Я]){0,}([ ])*([, ])*([\\.]){0,})+");

    public JLabel jLabelClock;
    private JFrame frame;
    private JMenuBar menu;
    private JTextField textField;
    private JFormattedTextField fieldForDate;
    private JLabel label;
    private static Properties props;
    private JPanel addRemovePanel;
    private JButton updateLookAndFeelButton;
    private JComboBox comboBox;
    private JButton addFavorite;
    private JButton deleteFavorite;
    private JButton setFavorite;

    private Pattern pattern;
    private Matcher matcher;

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int sizeWidth = 700;
    private static int sizeHeight = 500;
    private static int locationX = (screenSize.width - sizeWidth);
    private static int locationY = (screenSize.height - sizeHeight);

    private DefaultListModel<MyItem> names = new DefaultListModel<>();
    private JList<MyItem> listBox = new JList<>(names);

    private DefaultListModel<String> fav = new DefaultListModel<>();
    private JList<String> favorite = new JList<>(fav);

    private int[] arr;
    private int[] arr2;

    private static final String RB_NAME = "config.main";
    private static final String PROP_LANGS = "langs";
    private static final String PROP_LANGS_DEFAULT = "lang.default";
    private String namesDescription;
    private Logger logger = Logger.getLogger("MyLog");
    private Color col;
    private String date;

    private MyLocale myLocale = new MyLocale(RB_NAME);

    private GUI() {
        super();

        props = System.getProperties();
        getLangsProperties();
        createMenuBar(langSet, defaultLang);
        myTime();

        JList<String> list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(list);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(listScrollPane, BorderLayout.WEST);

        ActionListener updateButtonListener = new UpdateListAction(list);
        updateButtonListener.actionPerformed(
                new ActionEvent(list, ActionEvent.ACTION_PERFORMED, null)
        );

        updateLookAndFeelButton = new JButton();
        updateLookAndFeelButton.putClientProperty(MyLocale.LOCALIZATION_KEY, "btn.updateLookAndFeelButton");

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.CENTER);

        textField = new JTextField();
        textField.setDocument(new Limit(30));
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (textField.getText().length() > 1 & textField.getText().length() < 30) {
                    label.setText("");
                } else
                    label.setText(myLocale.getStringResource("lbl.symbols"));
            }
        });

        JScrollPane tasksScrollPane = new JScrollPane(listBox);
        JScrollPane favoriteTasks = new JScrollPane(favorite);
        favoriteTasks.setPreferredSize(new Dimension(150, 0));
        favorite.setBackground(new Color(223, 220, 225));
        readTask();
        readFavorite();
        deadTask();
        myButtons();

        label = new JLabel();
        label.setForeground(Color.red);
        label.setFont(new Font("Comin Sans MS", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        String[] items = {"★ ★ ★ ★", "★ ★ ★", "★ ★", "★"};
        comboBox = new JComboBox(items);

        textField.setPreferredSize(new Dimension(250, 25));
        try {
            MaskFormatter maskFormatter = new MaskFormatter("##.##.####");
            maskFormatter.setPlaceholderCharacter('_');
            fieldForDate = new JFormattedTextField(maskFormatter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fieldForDate.setPreferredSize(new Dimension(68, 25));

        JPanel demo = new JPanel();
        demo.add(textField, BorderLayout.WEST);
        demo.add(comboBox, BorderLayout.CENTER);
        demo.add(fieldForDate, BorderLayout.EAST);

        panel.add(addRemovePanel, BorderLayout.SOUTH);
        topPanel.add(tasksScrollPane, BorderLayout.CENTER);
        topPanel.add(favoriteTasks, BorderLayout.EAST);
        topPanel.add(demo, BorderLayout.NORTH);

        Image icon = new ImageIcon(IMAGE_PATH).getImage();

        String FRAME = "Taasky";
        frame = new JFrame(FRAME);
        frame.setIconImage(icon);
        frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setJMenuBar(menu);
        frame.add(label, BorderLayout.SOUTH);
        frame.add(jLabelClock, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        WindowListener listener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent w) {
                writeTask();
                writeFavorite();
                System.exit(0);
            }
        };
        frame.addWindowListener(listener);

        updateLookAndFeelButton.addActionListener(
                new UpdateLookAndFeelAction(frame, list)
        );

        applyLocale(defaultLang);

        listBox.addListSelectionListener(e -> {
            if (!listBox.isSelectionEmpty()) {
                listBox.setSelectionBackground(listBox.getSelectedValue().getStatus());
            }
        });
    }

    private void applyLocale(String locale) {
        myLocale.setLocale(locale);
        myLocale.changeLocale(frame);
        myLocale.changeLocale(getJMenuBar());
    }

    private void myTime() {
        jLabelClock = new JLabel();
        jLabelClock.setFont(new Font("Comin Sans MS", Font.BOLD, 16));
        add(jLabelClock);
        ClockThread ct = new ClockThread(GUI.this);
        jLabelClock.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelClock.setVerticalAlignment(SwingConstants.CENTER);
    }

    public static void getLangsProperties() {
        InputStream input;
        InputStreamReader inputStreamReader = null;
        try {
            input = new FileInputStream(INPUT_S);
            inputStreamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert inputStreamReader != null;
            props.load(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String langsProp = props.getProperty(PROP_LANGS);
        defaultLang = props.getProperty(PROP_LANGS_DEFAULT);
        langSet = new TreeSet<>(Arrays.asList(langsProp.split(",")));
    }

    private void createMenuBar(Set<String> languages, String defaultLanguage) {
        menu = new JMenuBar();
        setJMenuBar(menu);

        JMenu mFile = new JMenu();
        mFile.putClientProperty(MyLocale.LOCALIZATION_KEY, "menu.file");
        JMenu mLang = new JMenu();
        mLang.putClientProperty(MyLocale.LOCALIZATION_KEY, "menu.language");
        JMenu mHistory = new JMenu();
        mHistory.putClientProperty(MyLocale.LOCALIZATION_KEY, "menu.history");
        JMenuItem hi = new JMenuItem();
        hi.putClientProperty(MyLocale.LOCALIZATION_KEY, "menu.history.deleted");
        mHistory.add(hi);

        hi.addActionListener(e -> {
            writeTask();
            writeFavorite();
            new History();
        });

        menu.add(mFile);
        menu.add(mLang);
        menu.add(mHistory);

        JMenuItem miExit = new JMenuItem();
        miExit.putClientProperty(MyLocale.LOCALIZATION_KEY, "menu.file.exit");
        mFile.add(miExit);
        miExit.addActionListener(e -> {
            writeTask();
            writeFavorite();
            System.exit(0);
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        for (final String lang : languages) {
            JRadioButtonMenuItem menuLangs = new JRadioButtonMenuItem("", false);
            menuLangs.putClientProperty(MyLocale.LOCALIZATION_KEY, "menu." + lang + ".desc");
            buttonGroup.add(menuLangs);
            mLang.add(menuLangs);
            if (lang.equals(defaultLanguage)) {
                menuLangs.setSelected(true);
            }
            menuLangs.addActionListener(e -> applyLocale(lang));
        }
    }

    private void writeTask() {
        List<MyItem> myItems = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            myItems.add(names.get(i));
        }
        XMLHelper.writeToXML(myItems);
    }

    private void writeHistory() {
        try {
            OutputStream f = new FileOutputStream(HISTORY, true);
            OutputStreamWriter writer = new OutputStreamWriter(f);
            BufferedWriter out = new BufferedWriter(writer);
            out.write(namesDescription + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFavorite() {
        try {
            OutputStream f2 = new FileOutputStream(FAVORITE, false);
            OutputStreamWriter writer2 = new OutputStreamWriter(f2);
            BufferedWriter out2 = new BufferedWriter(writer2);
            for (int i = 0; i < fav.size(); i++) {
                out2.write(fav.get(i) + "\n");
                out2.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readTask() {
        List<MyItem> myItems = (List<MyItem>) XMLHelper.readFromXML();
        if (myItems != null) {
            for (int i = 0; i < myItems.size(); i++) {
                names.add(i, myItems.get(i));
            }
        }
        listBox.setModel(names);
    }

    private void readFavorite() {
        try (BufferedReader BR = new BufferedReader(new FileReader(FAVORITE))) {
            String item;
            while ((item = BR.readLine()) != null) {
                fav.addElement(item);
            }
            favorite.setModel(fav);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void myButtons() {
        JButton addButton = new JButton();
        addButton.putClientProperty(MyLocale.LOCALIZATION_KEY, "btn.add");
        addButton.addActionListener(e -> {
            pattern = Pattern.compile(LIST_OF_SYMBOLS);
            matcher = pattern.matcher(textField.getText());
            int color = comboBox.getSelectedIndex();
            col = Color.white;
            switch (color) {
                case 0:
                    col = Color.red;
                    break;
                case 1:
                    col = Color.orange;
                    break;
                case 2:
                    col = Color.yellow;
                    break;
                case 3:
                    col = Color.green;
                    break;
            }

            date = fieldForDate.getText();

            long curTime = System.currentTimeMillis();
            Date curDate = new Date(curTime);
            try {
                if (curDate.before(MyDate.stringToDate(date)) | MyDate.dateToString(curDate).equals(date)) {
                    if (textField.getText().length() <= 30 & textField.getText().length() >= 3) {
                        if (!matcher.matches()) {
                            label.setText(myLocale.getStringResource("lbl.label"));
                            fieldForDate.setText("");
                            textField.setText("");
                        } else {
                            MyItem item = new MyItem(textField.getText(), col, MyDate.stringToDate(date));
                            names.add(names.getSize(), item);
                            deadTask();
                            textField.setText("");
                            label.setText("");
                            fieldForDate.setText("");
                        }
                    }
                } else {
                    label.setText(myLocale.getStringResource("lbl.wrong.date"));
                }
            } catch (DateTimeParseException ex) {
                fieldForDate.setText("");
                label.setText(myLocale.getStringResource("lbl.wrong.date"));
                logger.info("user not fully entered date");
            }
        });

        JButton removeButton = new JButton();
        removeButton.putClientProperty(MyLocale.LOCALIZATION_KEY, "btn.delete");
        removeButton.addActionListener(e -> {
            arr = listBox.getSelectedIndices();
            for (int i1 : arr) {
                namesDescription = names.get(i1).getDescription();
                names.remove(i1);
                logger.info("added to history");
                writeHistory();
            }
        });

        JButton editButton = new JButton();
        editButton.putClientProperty(MyLocale.LOCALIZATION_KEY, "btn.edit");
        if (listBox.isSelectionEmpty()) {
            editButton.addActionListener(e -> {
                int index = listBox.getSelectedIndex();
                textField.setText(listBox.getSelectedValue().getDescription());
                fieldForDate.setText(MyDate.dateToString(listBox.getSelectedValue().getTime()));
                names.removeElementAt(index);
            });

            addFavorite = new JButton();
            addFavorite.setText("+");
            addFavorite.addActionListener(e -> {
                pattern = Pattern.compile(LIST_OF_SYMBOLS);
                matcher = pattern.matcher(textField.getText());
                if (textField.getText().length() <= 30 & textField.getText().length() >= 3) {
                    if (!matcher.matches()) {
                        label.setText(myLocale.getStringResource("lbl.label"));
                        textField.setText("");
                    } else {
                        fav.add(fav.getSize(), textField.getText());
                        textField.setText("");
                        label.setText("");
                    }
                }
            });

            deleteFavorite = new JButton();
            deleteFavorite.setText("-");
            deleteFavorite.addActionListener(e -> {
                arr2 = favorite.getSelectedIndices();
                for (int i1 : arr2) {
                    fav.remove(i1);
                }
            });

            setFavorite = new JButton();
            setFavorite.setText("√");
            setFavorite.addActionListener(e -> textField.setText(favorite.getSelectedValue()));
        }

        addRemovePanel = new JPanel();
        addRemovePanel.add(updateLookAndFeelButton);
        addRemovePanel.add(addButton);
        addRemovePanel.add(removeButton);
        addRemovePanel.add(editButton);
        addRemovePanel.add(addFavorite);
        addRemovePanel.add(deleteFavorite);
        addRemovePanel.add(setFavorite);

    }

    private void deadTask() {
        for (int i = 0; i < names.size(); i++) {
            long curTime = System.currentTimeMillis();
            Date curDate = new Date(curTime);

            if (MyDate.dateToString(curDate).equals(MyDate.dateToString(names.get(i).getTime()))) {
                if (names.get(i).getDescription().contains("   TODAY")) {
                    logger.info("find item for today ");
                } else {
                    names.get(i).setDescription(names.get(i).getDescription() + "   TODAY");
                }

            } else {
                //1558990800000 - 28/05/2019
                //1559077200000 - 29/05/2019
                if (names.get(i).getDescription().contains("   IT'S ALREADY IMPOSSIBLE")) {
                    logger.info("delete this, please ");
                } else if (names.get(i).getDescription().contains("   TODAY")) {
                    names.get(i).setDescription(names.get(i).getDescription() + "   IT'S ALREADY IMPOSSIBLE");
                    logger.info("lolol ");
                }

            }
        }

    }

    public static void main(String[] args) {
        new GUI();
    }
}