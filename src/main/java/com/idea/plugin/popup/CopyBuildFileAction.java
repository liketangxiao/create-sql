package com.idea.plugin.popup;

import com.idea.plugin.build.BuildService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public class CopyBuildFileAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        PsiFile requiredData = e.getRequiredData(CommonDataKeys.PSI_FILE);
        BuildService.copyBuildFile(project, requiredData.getViewProvider().getVirtualFile());
    }
}
