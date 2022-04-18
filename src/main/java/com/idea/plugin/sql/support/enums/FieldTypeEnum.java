package com.idea.plugin.sql.support.enums;

import com.idea.plugin.sql.support.exception.SqlException;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum FieldTypeEnum {
    STRING("VARCHAR(%s)", "NVARCHAR2(%s)"),
    TIMESTAMP("TIMESTAMP", "TIMESTAMP"),
    TEXT("TEXT", "CLOB"),
    NUMBER("DECIMAL(%s)", "NUMBER(%s)"),
    ;
    private String mtype;
    private String otype;

    FieldTypeEnum(String mtype, String otype) {
        this.mtype = mtype;
        this.otype = otype;
    }

    public static FieldTypeEnum codeToEnum(String code) throws SqlException {
        Optional<FieldTypeEnum> fieldTypeEnumOptional = Arrays.stream(FieldTypeEnum.values()).filter(fieldTypeEnum -> code.startsWith(fieldTypeEnum.name())).findAny();
        if (fieldTypeEnumOptional.isPresent()) {
            return fieldTypeEnumOptional.get();
        }
        throw new SqlException(String.format("字段类型%s不存在", code));
    }

    public static String codeGetArgs(String code) throws SqlException {
        Pattern patternCode = Pattern.compile("(?<=\\()[^\\)]+");
        Matcher matcherCode = patternCode.matcher(code);
        if (matcherCode.find()) {
            return matcherCode.group();
        }
        if (STRING.equals(codeToEnum(code))) {
            return "32";
        }
        if (NUMBER.equals(codeToEnum(code))) {
            return "32, 6";
        }
        return null;
    }

    public String getMtype(Object... args) {
        return String.format(mtype, args);
    }

    public String getOtype(Object... args) {
        return String.format(otype, args);
    }
}
