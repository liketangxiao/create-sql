package com.idea.plugin.sql.support.enums;

import java.util.Arrays;

public enum ProcedureTypeEnum {
    INITIAL(FileTypeEnum.INITIAL),
    ADD_TABLE(FileTypeEnum.CREATE),
    ADD_INDEX(FileTypeEnum.ALTER),
    ADD_COLUMN(FileTypeEnum.ALTER),
    ADD_DATA(FileTypeEnum.INSERT),
    INSERT_DATA(FileTypeEnum.INSERT),
    INSERT_SQL(FileTypeEnum.INSERT),
    UPDATE_DATA(FileTypeEnum.UPDATE),
    ;
    private FileTypeEnum fileType;

    public FileTypeEnum getFileType() {
        return fileType;
    }

    ProcedureTypeEnum(FileTypeEnum fileType) {
        this.fileType = fileType;
    }

    public static ProcedureTypeEnum codeToEnum(String code) {
        return Arrays.stream(ProcedureTypeEnum.values()).filter(procedureTypeEnum -> procedureTypeEnum.name().equals(code)).findAny().orElse(null);
    }

}
