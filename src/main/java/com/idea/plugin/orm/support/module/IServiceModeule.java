package com.idea.plugin.orm.support.module;

import com.idea.plugin.orm.support.GeneratorContext;
import com.idea.plugin.orm.support.TableModule;
import com.idea.plugin.orm.support.enums.FileTypePathEnum;

import java.util.HashSet;
import java.util.Set;

public class IServiceModeule extends TableModule {

    public IServiceModeule(GeneratorContext context) {
        super(context);
    }

    public Set<String> getImports() {
        Set<String> imports = new HashSet<>();
        fileTypeInfo.setImports(FileTypePathEnum.ENTITY, imports, tableInfoVO.tableName);
        fileTypeInfo.setImports(FileTypePathEnum.VO, imports, tableInfoVO.tableName);
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
        return FileTypePathEnum.ISERVICE.getFileName(tableInfoVO.tableName);
    }
}
