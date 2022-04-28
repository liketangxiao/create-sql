package com.idea.plugin.window;

import com.idea.plugin.ui.SettingUI;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 配置页窗体
 *
 * @author wangyuj
 * @date 2022/04/28
 */
public class SettingFactory implements SearchableConfigurable {
    private static SettingUI settingUI = new SettingUI();

    @Override
    public @NotNull
    String getId() {
        return "Create.File";
    }

    @Override
    public @NotNull
    String getDisplayName() {
        return "Create File";
    }

    @Override
    public @Nullable
    JComponent createComponent() {
        return settingUI.getMianPanel();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() {

    }
}
