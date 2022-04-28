package com.idea.plugin.ui;

import com.idea.plugin.build.BuildSettings;
import com.idea.plugin.demo.DemoSettings;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.util.concurrent.ConcurrentHashMap;

public class SettingUI {

    private JPanel mianPanel;
    private JButton removeDemoFileCacheButton;
    private JButton removeCopyFileCacheButton;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private BuildSettings buildconfig;
    private DemoSettings democonfig;


    public SettingUI() {
        buildconfig = BuildSettings.getInstance(ProjectManager.getInstance().getDefaultProject());
        democonfig = DemoSettings.getInstance(ProjectManager.getInstance().getDefaultProject());
        removeDemoFileCacheButton.addActionListener(e -> {
            democonfig.getDemoConfigVO().tabNameCacheMap = new ConcurrentHashMap<>();
            democonfig.getDemoConfigVO().tableInfoCacheMap = new ConcurrentHashMap<>();
            Messages.showMessageDialog("success", "正确", Messages.getInformationIcon());
        });
        removeCopyFileCacheButton.addActionListener(e -> {
            buildconfig.getBuildConfigVO().filePathCache = null;
            Messages.showMessageDialog("success", "正确", Messages.getInformationIcon());
        });
    }

    public JComponent getMianPanel() {
        return mianPanel;
    }

}
