package com.idea.plugin.sql.support.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ProcedureTypeEnum {
    INITIAL(FileTypeEnum.INITIAL, Collections.emptyList()),
    ADD_TABLE(FileTypeEnum.CREATE, Arrays.asList(
            "tableName",
            "fieldInfos")),
    ADD_INDEX(FileTypeEnum.ALTER, Arrays.asList(
            "tableName",
            "indexInfos")),
    ADD_COLUMN(FileTypeEnum.ALTER, Arrays.asList(
            "tableName",
            "fieldInfos")),
    ADD_DATA(FileTypeEnum.INSERT, Arrays.asList(
            "tableName",
            "insertColumnName",
            "insertColumnParam")),
    INSERT_DATA(FileTypeEnum.INSERT, Arrays.asList(
            "tableName",
            "insertData")),
    INSERT_SQL(FileTypeEnum.INSERT, Arrays.asList(
            "jdbcUrl",
            "username",
            "password",
            "tableName",
            "insertSql")),
    ;
    private FileTypeEnum fileType;
    private List<String> mustFieldList;

    ProcedureTypeEnum(FileTypeEnum fileType, List<String> mustFieldList) {
        this.fileType = fileType;
        this.mustFieldList = mustFieldList;
    }

    public FileTypeEnum getFileType() {
        return fileType;
    }

    public List<String> getMustFieldList() {
        return mustFieldList;
    }

    public static ProcedureTypeEnum codeToEnum(String code) {
        return Arrays.stream(ProcedureTypeEnum.values()).filter(procedureTypeEnum -> procedureTypeEnum.name().equals(code.toUpperCase())).findAny().orElse(null);
    }

}
