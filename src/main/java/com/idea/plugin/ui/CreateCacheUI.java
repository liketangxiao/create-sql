package com.idea.plugin.ui;

import com.idea.plugin.build.BuildSettings;
import com.idea.plugin.demo.DemoSettings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.util.concurrent.ConcurrentHashMap;

public class CreateCacheUI {
    private JPanel mainPanel;

    private JButton removeDemoFileCache;
    private JButton removeCopyFileCache;

    private BuildSettings buildconfig;
    private DemoSettings democonfig;

    private Project project;

    public CreateCacheUI() {
        buildconfig = BuildSettings.getInstance(ProjectManager.getInstance().getDefaultProject());
        democonfig = DemoSettings.getInstance(ProjectManager.getInstance().getDefaultProject());
        removeDemoFileCache.addActionListener(e -> {
            democonfig.getDemoConfigVO().tabNameCacheMap = new ConcurrentHashMap<>();
            democonfig.getDemoConfigVO().tableInfoCacheMap = new ConcurrentHashMap<>();
            Messages.showMessageDialog("success", "正确", Messages.getInformationIcon());
        });
        removeCopyFileCache.addActionListener(e -> {
            buildconfig.getBuildConfigVO().filePathCache = null;
            Messages.showMessageDialog("success", "正确", Messages.getInformationIcon());
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public JComponent getMainPanel() {
        return mainPanel;
    }
}
