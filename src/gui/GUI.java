package gui;

import localization.MyLocale;
import themes.UpdateListAction;
import themes.UpdateLookAndFeelAction;
import threads.ClockThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI extends JFrame {

    public JLabel jLabelClock;

    private String TIP_UPDATE = "Click to change your theme";
    private String TIP_TEXTFIELD = "Enter task";
    private String TIP_ADD = "Click to add";
    private String TIP_DELETE = "Click to remove";
    private String langsProp;
    private String defaultLang;
    private Set<String> langSet;

    private JFrame frame;
    private JMenuBar menu;
    private final JTextField textField;
    private final JLabel label;
    private Properties props;
    private ClockThread ct;
    private JPanel addRemovePanel;
    private JButton updateLookAndFeelButton;

    private Pattern pattern;
    private Matcher matcher;

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int sizeWidth = 400;
    private static int sizeHeight = 400;
    private static int locationX = (screenSize.width - sizeWidth);
    private static int locationY = (screenSize.height - sizeHeight);

    private DefaultListModel<String> names = new DefaultListModel<>();
    private JList<String> listBox = new JList<>(names);
    private int[] arr;

    private static final String RB_NAME = "config.main";
    private static final String PROP_LANGS = "langs";
    private static final String PROP_LANGS_DEFAULT = "lang.default";

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
        updateLookAndFeelButton.setToolTipText(TIP_UPDATE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.CENTER);

        textField = new JTextField();
        textField.setToolTipText(TIP_TEXTFIELD);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (textField.getText().length() >= 3 & textField.getText().length() <= 30) {
                    label.setText("");
                } else
                    label.setText(myLocale.getStringResource("lbl.symbols"));
            }
        });

        JScrollPane tasksScrollPane = new JScrollPane(listBox);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) listBox.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        addRemove();

        label = new JLabel();
        label.setForeground(Color.red);
        label.setFont(new Font("Comin Sans MS", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        panel.add(addRemovePanel, BorderLayout.SOUTH);
        topPanel.add(textField, BorderLayout.NORTH);
        topPanel.add(tasksScrollPane);

        Image icon = new ImageIcon("C:\\Users\\Masha\\IntelliJIDEAProjects\\AipLaba\\src\\config\\icon1.png").getImage();

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

        updateLookAndFeelButton.addActionListener(
                new UpdateLookAndFeelAction(frame, list)
        );

        applyLocale(defaultLang);
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
        ct = new ClockThread(GUI.this);
        jLabelClock.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelClock.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void getLangsProperties() {
        InputStream input;
        InputStreamReader inputStreamReader = null;
        try {
            input = new FileInputStream("C:\\Users\\Masha\\IntelliJIDEAProjects\\AipLaba\\src\\config\\langs.properties");
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
        langsProp = props.getProperty(PROP_LANGS);
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
        menu.add(mFile);
        menu.add(mLang);

        JMenuItem miExit = new JMenuItem();
        miExit.putClientProperty(MyLocale.LOCALIZATION_KEY, "menu.file.exit");
        mFile.add(miExit);
        miExit.addActionListener(e -> System.exit(0));

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

    private void addRemove() {
        JButton addButton = new JButton();
        addButton.putClientProperty(MyLocale.LOCALIZATION_KEY, "btn.add");
        addButton.setToolTipText(TIP_ADD);
        addButton.addActionListener(e -> {
            pattern = Pattern.compile("(([0-9]){0,}([a-zа-яA-ZА-Я]){0,}([ ])*([, ])*([\\.]){0,})+");
            matcher = pattern.matcher(textField.getText());

            if (textField.getText().length() <= 30 & textField.getText().length() >= 3) {
                if (!matcher.matches()) {
                    label.setText(myLocale.getStringResource("lbl.label"));
                    textField.setText("");
                } else {
                    names.add(names.getSize(), textField.getText());
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
                names.remove(i1);
            }
        });

        addRemovePanel = new JPanel();
        addRemovePanel.add(updateLookAndFeelButton);
        addRemovePanel.add(addButton);
        addRemovePanel.add(removeButton);
    }

    public static void main(String[] args) {
        new GUI();
    }
}
