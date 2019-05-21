package gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;

class History extends JFrame {

    private static final String HISTORY = ("history.txt");
    private static final String ICON = ("C:\\Users\\Masha\\IntelliJIDEAProjects\\AipLaba\\src\\config\\trash3.jpg");
    private static final String IMAGE_PATH = ("C:\\Users\\Masha\\IntelliJIDEAProjects\\AipLaba\\src\\config\\icon1.png");

    private JPanel addRemoveHistory;

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int sizeWidth = 300;
    private static int sizeHeight = 300;
    private static int locationX = (screenSize.width - sizeWidth);
    private static int locationY = (screenSize.height - sizeHeight);

    private DefaultListModel<String> hist = new DefaultListModel<>();
    private JList<String> listBox = new JList<>(hist);
    private int[] arr;

    History() {

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.CENTER);

        JScrollPane tasksScrollPane = new JScrollPane(listBox);
        readHistory();
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) listBox.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        addRemoveEdit();

        panel.add(addRemoveHistory, BorderLayout.SOUTH);
        topPanel.add(tasksScrollPane);

        String FRAME = "Taasky";
        Image icon2 = new ImageIcon(IMAGE_PATH).getImage();
        JFrame frame2 = new JFrame(FRAME);
        frame2.setIconImage(icon2);
        frame2.setBounds(locationX, locationY, sizeWidth, sizeHeight);
        frame2.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame2.add(panel);
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
        frame2.setResizable(false);

    }

    private void writeHistory() {
        try {
            OutputStream f = new FileOutputStream(HISTORY, false);
            OutputStreamWriter writer = new OutputStreamWriter(f);
            BufferedWriter out = new BufferedWriter(writer);
            for (int i = 0; i < hist.size(); i++) {
                out.write(hist.get(i) + "\n");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHistory() {

        try (BufferedReader BR = new BufferedReader(new FileReader(new File(HISTORY)))) {
            String item;
            while ((item = BR.readLine()) != null) {
                hist.addElement(item);
            }
            listBox.setModel(hist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addRemoveEdit() {

        Icon icon = new ImageIcon(new ImageIcon(ICON).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

        JButton removeHistory = new JButton(icon);
        removeHistory.addActionListener(e -> {
            arr = listBox.getSelectedIndices();
            for (int i1 : arr) {
                hist.remove(i1);
                writeHistory();
            }
        });

        addRemoveHistory = new JPanel();
        addRemoveHistory.add(removeHistory);
    }

}
