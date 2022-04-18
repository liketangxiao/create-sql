package com.idea.plugin.ui;

import javax.swing.*;
import java.io.File;

public class SettingUI {

    private JPanel mianPanel;
    private JPanel settingPanel;
    private JTextField urlTextField;
    private JLabel urlLabel;
    private JButton urlButton;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public SettingUI() {
        urlButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jFileChooser.showOpenDialog(settingPanel);
            File selectedFile = jFileChooser.getSelectedFile();
            urlTextField.setText(selectedFile.getPath());
        });
    }

    public JComponent getMianPanel() {
        return mianPanel;
    }

    public JTextField getUrlTextField() {
        return urlTextField;
    }
}
