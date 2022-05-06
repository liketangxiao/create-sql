package com.idea.plugin.orm.support.module;

import com.idea.plugin.orm.support.GeneratorContext;
import com.idea.plugin.orm.support.TableModule;
import com.idea.plugin.orm.support.enums.FileTypePathEnum;
import org.apache.commons.lang3.StringUtils;

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
        if (StringUtils.isNotEmpty(tableInfoVO.controllerReturn)) {
            imports.add(tableInfoVO.controllerReturn);
        }
        imports.add("org.springframework.beans.factory.annotation.Autowired");
        imports.add("org.springframework.web.bind.annotation.*");
        imports.add("java.util.List");

        return imports;
    }

    public String getModuleName() {
        return fileTypeInfo.getModuleName();
    }

    public String getReturn() {
        String aReturn = tableInfoVO.controllerReturn;
        if (StringUtils.isEmpty(aReturn)) {
            return "ResultValue";
        }
        return aReturn.substring(aReturn.lastIndexOf(".") + 1);
    }
}
