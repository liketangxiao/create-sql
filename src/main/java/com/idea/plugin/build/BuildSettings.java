package com.idea.plugin.build;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "BuildSettings", storages = {@Storage("plugin.xml")})
public class BuildSettings implements PersistentStateComponent<BuildConfigVO> {

    private BuildConfigVO buildConfigVO = new BuildConfigVO();

    @NotNull
    public static BuildSettings getInstance(Project project) {
        return ServiceManager.getService(project, BuildSettings.class);
    }

    @Override
    public @Nullable BuildConfigVO getState() {
        return buildConfigVO;
    }

    @Override
    public void loadState(@NotNull BuildConfigVO demoConfigVO) {
        this.buildConfigVO = demoConfigVO;
    }


    public BuildConfigVO getBuildConfigVO() {
        return buildConfigVO;
    }

}
