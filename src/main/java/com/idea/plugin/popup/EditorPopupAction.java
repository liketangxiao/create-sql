package com.idea.plugin.popup;

import com.idea.plugin.sql.CreateSqlFile;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * 右键文件内容时触发
 *
 * @author yanzhao
 * @date 2022/1/26 13:44
 * @since 1.0.0
 */
public class EditorPopupAction extends AnAction {

    static String SQL_PATH = "D:/git/ECS/DEV/java-web/sql/fssc/";

    /**
     * 当菜单或者按钮被点击时，触发这个方法
     * 在这个方法中，可以弹出swing做的窗口
     *
     * @param e 事件对象，idea注入的对象，包括了整个项目的AST等等的内容
     *          可以通过e.getRequiredData等方法，获取一些数据，当然还有其他的方法，自己尝试一下
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        DataContext dataContext = e.getDataContext();
        PsiFile requiredData = e.getRequiredData(CommonDataKeys.PSI_FILE);
        Document document = requiredData.getViewProvider().getDocument();
        CreateSqlFile createSqlFile = new CreateSqlFile();
        createSqlFile.createSqlFileByText(document.getText());
    }


    /**
     * 显示菜单时，idea会调用此方法
     * 用户可以在此方法中，进行自定义校验，判断菜单或按钮是否置灰
     * 例如：当且仅当java文件时，菜单或按钮可用，其他不可用
     * e.getPresentation().setEnabled(false);
     *
     * @param e 事件对象，idea注入的对象，包括了整个项目的AST等等的内容
     *          可以通过e.getRequiredData等方法，获取一些数据，当然还有其他的方法，自己尝试一下
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
    }

}
