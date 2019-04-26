package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateLookAndFeelAction implements ActionListener {
    private JList<String> list;
    private JFrame rootFrame;

    private String CHANGED = "Theme was changed to ";
    private String ERROR = "Error: ";
    private String NOT_FOUND = "not found";
    private String INSTALLATION = "Error: instantiation exception";
    private String ILLEGAL = "Error: illegal access";
    private String UNSUPPORTED = "Error: unsupported look and feel";

    public UpdateLookAndFeelAction(JFrame frame, JList<String> list) {
        this.rootFrame = frame;
        this.list = list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String lookAndFeelName = list.getSelectedValue();
        UIManager.LookAndFeelInfo[] infoArray =
                UIManager.getInstalledLookAndFeels();

        for(UIManager.LookAndFeelInfo info : infoArray) {
            if(info.getName().equals(lookAndFeelName)) {
                String message = CHANGED + lookAndFeelName;
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    SwingUtilities.updateComponentTreeUI(rootFrame);
                } catch (ClassNotFoundException e1) {
                    message = ERROR + info.getClassName() + NOT_FOUND;
                } catch (InstantiationException e1) {
                    message = INSTALLATION;
                } catch (IllegalAccessException e1) {
                    message = ILLEGAL;
                } catch (UnsupportedLookAndFeelException e1) {
                    message = UNSUPPORTED;
                }
                JOptionPane.showMessageDialog(null, message);
                break;
            }
        }
    }
}
