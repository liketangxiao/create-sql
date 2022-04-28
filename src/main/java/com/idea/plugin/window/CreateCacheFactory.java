package com.idea.plugin.window;

import com.idea.plugin.ui.CreateCacheUI;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * 配置侧边窗体
 *
 * @author wangyuj
 * @date 2022/04/28
 */
public class CreateCacheFactory implements ToolWindowFactory {

    private CreateCacheUI createCacheUI = new CreateCacheUI();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        createCacheUI.setProject(project);
        ContentFactory instance = ContentFactory.SERVICE.getInstance();
        Content content = instance.createContent(createCacheUI.getMainPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
