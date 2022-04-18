package com.idea.plugin.window;

import com.idea.plugin.ui.ReadingUI;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class ReadFactory implements ToolWindowFactory {

    private ReadingUI readingUI = new ReadingUI();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory instance = ContentFactory.SERVICE.getInstance();
        Content content = instance.createContent(readingUI.getMainPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
        ReadConfig.readingUI = readingUI;
    }
}
