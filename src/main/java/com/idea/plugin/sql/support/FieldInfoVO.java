package com.idea.plugin.sql.support;

import com.idea.plugin.sql.support.enums.FieldTypeEnum;
import com.idea.plugin.sql.support.enums.NullTypeEnum;
import com.idea.plugin.sql.support.enums.PrimaryTypeEnum;

public class FieldInfoVO {
    public String columnName;
    public FieldTypeEnum columnType;
    public String columnTypeArgs;
    public String comment;
    public NullTypeEnum nullType = NullTypeEnum.NULL;
    public PrimaryTypeEnum primary = PrimaryTypeEnum.NON_PRIMARY;

    public static FieldInfoVO builder() {
        return new FieldInfoVO();
    }


    public FieldInfoVO columnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public FieldInfoVO columnType(FieldTypeEnum columnType) {
        this.columnType = columnType;
        return this;
    }

    public FieldInfoVO columnTypeArgs(String columnTypeArgs) {
        this.columnTypeArgs = columnTypeArgs;
        return this;
    }

    public FieldInfoVO comment(String comment) {
        this.comment = comment;
        return this;
    }

    public FieldInfoVO nullType(NullTypeEnum nullType) {
        this.nullType = nullType;
        return this;
    }

    public FieldInfoVO primary(PrimaryTypeEnum primary) {
        this.primary = primary;
        return this;
    }
}
