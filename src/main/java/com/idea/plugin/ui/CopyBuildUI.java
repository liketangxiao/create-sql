package com.idea.plugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CopyBuildUI extends DialogWrapper {
    private JPanel jPanel;
    private JTextField pathText;

    public CopyBuildUI(@Nullable Project project) {
        super(project);
        super.setSize(400,30);
        super.init();
        setTitle("请输入文件名称");
    }

    public String getFolderName() {
        return pathText.getText();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return jPanel;
    }
}
