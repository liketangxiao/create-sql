package com.idea.plugin.orm.support.module;

import com.idea.plugin.orm.support.GeneratorContext;
import com.idea.plugin.orm.support.TableModule;
import com.idea.plugin.orm.support.enums.FileTypePathEnum;

import java.util.HashSet;
import java.util.Set;

public class VoModeule extends TableModule {

    public VoModeule(GeneratorContext context) {
        super(context);
    }

    public Set<String> getImports() {
        Set<String> imports = new HashSet<>();
        fileTypeInfo.setImports(FileTypePathEnum.ENTITY, imports, tableInfoVO.tableName);
        return imports;
    }


    public String getEntityName() {
        return FileTypePathEnum.ENTITY.getFileName(tableInfoVO.tableName);
    }

}
