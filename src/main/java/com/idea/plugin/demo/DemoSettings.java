package com.idea.plugin.demo;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "DemoSettings", storages = {@Storage("plugin.xml")})
public class DemoSettings implements PersistentStateComponent<DemoConfigVO> {

    private DemoConfigVO demoConfigVO = new DemoConfigVO();

    @NotNull
    public static DemoSettings getInstance(Project project) {
        return ServiceManager.getService(project, DemoSettings.class);
    }

    @Override
    public @Nullable DemoConfigVO getState() {
        return demoConfigVO;
    }

    @Override
    public void loadState(@NotNull DemoConfigVO demoConfigVO) {
        this.demoConfigVO = demoConfigVO;
    }


    public DemoConfigVO getDemoConfigVO() {
        return demoConfigVO;
    }

    public void setProcedureType(String type) {
        if (!demoConfigVO.getProcedureTypeList().contains(type)) {
            demoConfigVO.getProcedureTypeList().add(type);
        }
    }

}
