package com.idea.plugin.orm.support.module;

import com.idea.plugin.orm.support.GeneratorContext;
import com.idea.plugin.orm.support.TableModule;
import com.idea.plugin.orm.support.enums.FileTypePathEnum;

public class MapperModeule extends TableModule {

    public MapperModeule(GeneratorContext context) {
        super(context);
    }

    public String getEntityNamePath() {
        return (FileTypePathEnum.DO.getJavapath(fileTypeInfo.getModulePath()) + "/" + FileTypePathEnum.DO.getFileName(tableInfoVO.tableName)).replaceAll("/", ".");
    }

    public String getMapperNamePath() {
        return (FileTypePathEnum.MAPPER.getJavapath(fileTypeInfo.getModulePath()) + "/" + FileTypePathEnum.MAPPER.getFileName(tableInfoVO.tableName)).replaceAll("/", ".");
    }

}
