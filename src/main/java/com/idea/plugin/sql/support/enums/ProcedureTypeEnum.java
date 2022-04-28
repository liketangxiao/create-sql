package com.idea.plugin.sql.support.enums;

import com.idea.plugin.orm.support.enums.FileCreateTypeEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ProcedureTypeEnum {
    INITIAL(FileDDLTypeEnum.INITIAL, Collections.emptyList()),
    ADD_TABLE(FileDDLTypeEnum.CREATE, Arrays.asList(
            "tableName",
            "fieldInfos")),
    ADD_INDEX(FileDDLTypeEnum.ALTER, Arrays.asList(
            "tableName",
            "indexInfos")),
    ADD_COLUMN(FileDDLTypeEnum.ALTER, Arrays.asList(
            "tableName",
            "fieldInfos")),
    ADD_DATA(FileDDLTypeEnum.INSERT, Arrays.asList(
            "tableName",
            "insertColumnName",
            "insertColumnParam")),
    INSERT_DATA(FileDDLTypeEnum.INSERT, Arrays.asList(
            "tableName",
            "insertData")),
    INSERT_SQL(FileDDLTypeEnum.INSERT, Arrays.asList(
            "jdbcUrl",
            "username",
            "password",
            "tableName",
            "insertSql")),
    DO(FileCreateTypeEnum.DO, Arrays.asList("modulePath")),
    DAO(FileCreateTypeEnum.DAO, Arrays.asList("modulePath")),
    SERVICE(FileCreateTypeEnum.SERVICE, Arrays.asList("modulePath")),
    CONTROLLER(FileCreateTypeEnum.CONTROLLER, Arrays.asList("modulePath")),
    ;
    private FileDDLTypeEnum fileType;
    private FileCreateTypeEnum fileCreateType;
    private List<String> mustFieldList;

    ProcedureTypeEnum(FileDDLTypeEnum fileType, List<String> mustFieldList) {
        this.fileType = fileType;
        this.mustFieldList = mustFieldList;
    }

    ProcedureTypeEnum(FileCreateTypeEnum fileCreateType, List<String> mustFieldList) {
        this.fileCreateType = fileCreateType;
        this.mustFieldList = mustFieldList;
    }

    public FileDDLTypeEnum getFileType() {
        return fileType;
    }

    public FileCreateTypeEnum getFileCreateType() {
        return fileCreateType;
    }

    public List<String> getMustFieldList() {
        return mustFieldList;
    }

    public static ProcedureTypeEnum codeToEnum(String code) {
        return Arrays.stream(ProcedureTypeEnum.values()).filter(procedureTypeEnum -> procedureTypeEnum.name().equals(code.toUpperCase())).findAny().orElse(null);
    }

}
