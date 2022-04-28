package com.idea.plugin.demo;

import com.idea.plugin.ui.CreateDemoFileUI;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class DemoGenerateDialogWrapper extends DialogWrapper {

    private static final Logger logger = LoggerFactory.getLogger(DemoGenerateDialogWrapper.class);

    private CreateDemoFileUI createDemoFileUI = new CreateDemoFileUI();

    private Project project;
    private String selectFilePath;

    public DemoGenerateDialogWrapper(@Nullable Project project) {
        super(project);
        setCancelButtonText("Cancel");
        super.init();
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return createDemoFileUI.getMianPanel();
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        fillData(project, selectFilePath);
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
        fillData(project, selectFilePath);
        createDemoFileUI.removeCache();
    }


    public void fillData(Project project, String selectFilePath) {
        this.project = project;
        this.selectFilePath = selectFilePath;
        createDemoFileUI.fillData(project,selectFilePath);
    }

}
