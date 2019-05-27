package gui;

import localization.MyLocale;
import themes.UpdateListAction;
import themes.UpdateLookAndFeelAction;
import threads.ClockThread;
import other.Limit;
import other.XMLHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI extends JFrame {

    private static final String HISTORY = ("history.txt");
    private static final String FAVORITE = ("favorite.txt");

    private static final String IMAGE_PATH = ("C:\\Users\\Masha\\IntelliJIDEAProjects\\AipLaba\\src\\config\\icon1.png");
    private static final String INPUT_S = ("C:\\Users\\Masha\\IntelliJIDEAProjects\\AipLaba\\src\\config\\langs.properties");

    private String TIP_UPDATE = "Click to change your theme";
    private String TIP_TEXTFIELD = "Enter task";
    private String TIP_ADD = "Click to add";
    private String TIP_DELETE = "Click to remove";
    private String TIP_EDIT = "Click to edit";
    private String TIP_FAV_ADD = "Click to add ♥";
    private String TIP_FAV_DELETE = "Click to remove ♥";
    private String TIP_FAV_SET = "Click to choose ♥";

    private static String defaultLang;
    private static Set<String> langSet;

    private String LIST_OF_SYMBOLS = ("(([0-9]){0,}([a-zа-яA-ZА-Я]){0,}([ ])*([, ])*([\\.]){0,})+");

    public JLabel jLabelClock;
    private JFrame frame;
    private JMenuBar menu;
    private final JTextField textField;
    private final JLabel label;
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

    private Color col;

    private MyLocale myLocale = new MyLocale(RB_NAME);

    private GUI() {
        super();

        props = System.getProperties();
        getLangsProperties();
        createMenuBar(langSet, defaultLang);
        myTime();

        String[] items = {
                "Важно|Срочно",
                "Важно|Не срочно",
                "Не важно|Срочно",
                "Не важно|Не срочно"

        };

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
        updateLookAndFeelButton.setToolTipText(TIP_UPDATE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.CENTER);

        textField = new JTextField();
        textField.setDocument(new Limit(30));
        textField.setToolTipText(TIP_TEXTFIELD);
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

        myButtons();

        label = new JLabel();
        label.setForeground(Color.red);
        label.setFont(new Font("Comin Sans MS", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);


        comboBox = new JComboBox(items);
        textField.setPreferredSize(new Dimension(300, 25));
        JPanel demo = new JPanel();
        demo.add(textField, BorderLayout.NORTH);
        demo.add(comboBox, BorderLayout.SOUTH);

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
        addButton.setToolTipText(TIP_ADD);
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

            if (textField.getText().length() <= 30 & textField.getText().length() >= 3) {
                if (!matcher.matches()) {
                    label.setText(myLocale.getStringResource("lbl.label"));
                    textField.setText("");
                } else {
                    names.add(names.getSize(), new MyItem(textField.getText(), col));
                    textField.setText("");
                    label.setText("");
                }
            }
        });

        JButton removeButton = new JButton();
        removeButton.putClientProperty(MyLocale.LOCALIZATION_KEY, "btn.delete");
        removeButton.setToolTipText(TIP_DELETE);
        removeButton.addActionListener(e -> {
            arr = listBox.getSelectedIndices();
            for (int i1 : arr) {
                namesDescription = names.get(i1).getDescription();
                names.remove(i1);
                writeHistory();
            }
        });

        JButton editButton = new JButton();
        editButton.putClientProperty(MyLocale.LOCALIZATION_KEY, "btn.edit");
        editButton.setToolTipText(TIP_EDIT);
        if (listBox.isSelectionEmpty()) {
            editButton.addActionListener(e -> {
                int index = listBox.getSelectedIndex();
                textField.setText(listBox.getSelectedValue().getDescription());
                names.removeElementAt(index);
            });

            addFavorite = new JButton();
            addFavorite.setText("+");
            addFavorite.setToolTipText(TIP_FAV_ADD);
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
            deleteFavorite.setToolTipText(TIP_FAV_DELETE);
            deleteFavorite.addActionListener(e -> {
                arr2 = favorite.getSelectedIndices();
                for (int i1 : arr2) {
                    fav.remove(i1);
                }
            });

            setFavorite = new JButton();
            setFavorite.setText("√");
            setFavorite.setToolTipText(TIP_FAV_SET);
            setFavorite.addActionListener(e -> {
                textField.setText(favorite.getSelectedValue());
            });
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

    public static void main(String[] args) {
        new GUI();
    }
}