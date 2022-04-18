package com.idea.plugin.sql.support.enums;

import java.util.Comparator;
import java.util.List;

public enum FileTypeEnum {
    CREATE("create", 1),
    ALTER("alter", 2),
    INSERT("insert", 3),
    UPDATE("update", 4),
    INITIAL("alter", 5),
    ;
    private String code;
    private Integer positon;

    FileTypeEnum(String code, Integer positon) {
        this.code = code;
        this.positon = positon;
    }

    public String getCode() {
        return code;
    }

    public Integer getPositon() {
        return positon;
    }

    public static FileTypeEnum getFirstFileType(List<FileTypeEnum> fileTypeEnumList) {
        return fileTypeEnumList.stream().min(Comparator.comparing(FileTypeEnum::getPositon)).orElse(INITIAL);
    }
}
