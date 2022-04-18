package com.idea.plugin.window;

import com.idea.plugin.ui.SettingUI;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class SettingFactory implements SearchableConfigurable {
    private static SettingUI settingUI = new SettingUI();

    @Override
    public @NotNull
    String getId() {
        return "setting.id";
    }

    @Override
    public @NotNull
    String getDisplayName() {
        return "Setting.Config";
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
        String url = settingUI.getUrlTextField().getText();
        // 设置文本信息
        try {
            File file = new File(url);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(0);
            byte[] bytes = new byte[1024 * 1024];
            int readSize = randomAccessFile.read(bytes);
            byte[] copy = new byte[readSize];
            System.arraycopy(bytes, 0, copy, 0, readSize);
            String str = new String(copy, StandardCharsets.UTF_8);
            // 设置内容
            ReadConfig.readingUI.getTextContent().setText(str);
        } catch (Exception ignore) {
        }

    }
}
