package com.idea.plugin.orm.support.module;

import com.idea.plugin.orm.support.GeneratorContext;
import com.idea.plugin.orm.support.TableModule;
import com.idea.plugin.orm.support.enums.FileTypePathEnum;

import java.util.HashSet;
import java.util.Set;

public class ServiceModeule extends TableModule {

    public ServiceModeule(GeneratorContext context) {
        super(context);
    }

    public Set<String> getImports() {
        Set<String> imports = new HashSet<>();
        fileTypeInfo.setImports(FileTypePathEnum.VO, imports, tableInfoVO.tableName);
        fileTypeInfo.setImports(FileTypePathEnum.MAPPER, imports, tableInfoVO.tableName);
        fileTypeInfo.setImports(FileTypePathEnum.ISERVICE, imports, tableInfoVO.tableName);
        imports.add("org.springframework.beans.factory.annotation.Autowired");
        imports.add("org.springframework.stereotype.Service");
        imports.add("java.util.ArrayList");
        imports.add("java.util.List");

        return imports;
    }

}
