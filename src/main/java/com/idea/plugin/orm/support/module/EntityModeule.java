package com.idea.plugin.orm.support.module;

import com.idea.plugin.orm.support.FieldModule;
import com.idea.plugin.orm.support.GeneratorContext;
import com.idea.plugin.orm.support.TableModule;

import java.util.HashSet;
import java.util.Set;

public class EntityModeule extends TableModule {

    public EntityModeule(GeneratorContext context) {
        super(context);
    }

    public Set<String> getImports() {
        Set<String> imports = new HashSet<>();
        for (FieldModule field : getFields()) {
            if (field.isImport()) {
                imports.add(field.getClassName());
            }
        }
        return imports;
    }
}
