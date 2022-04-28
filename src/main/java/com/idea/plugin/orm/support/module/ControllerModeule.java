package com.idea.plugin.orm.support.module;

import com.idea.plugin.orm.support.GeneratorContext;
import com.idea.plugin.orm.support.TableModule;
import com.idea.plugin.orm.support.enums.FileTypePathEnum;

import java.util.HashSet;
import java.util.Set;

public class ControllerModeule extends TableModule {

    public ControllerModeule(GeneratorContext context) {
        super(context);
    }

    public Set<String> getImports() {
        Set<String> imports = new HashSet<>();
        fileTypeInfo.setImports(FileTypePathEnum.VO, imports, tableInfoVO.tableName);
        fileTypeInfo.setImports(FileTypePathEnum.ISERVICE, imports, tableInfoVO.tableName);
        imports.add("org.springframework.beans.factory.annotation.Autowired");
        imports.add("org.springframework.web.bind.annotation.*");
        imports.add("java.util.List");

        return imports;
    }

    public String getEntityName() {
        return FileTypePathEnum.ENTITY.getFileName(tableInfoVO.tableName);
    }

    public String getVoName() {
        return FileTypePathEnum.VO.getFileName(tableInfoVO.tableName);
    }

    public String getServiceName() {
        return FileTypePathEnum.SERVICE.getFileName(tableInfoVO.tableName);
    }

    public String getIserviceName() {
        return FileTypePathEnum.ISERVICE.getFileName(tableInfoVO.tableName);
    }

    public String getModuleName() {
        return fileTypeInfo.getModuleName();
    }
}
