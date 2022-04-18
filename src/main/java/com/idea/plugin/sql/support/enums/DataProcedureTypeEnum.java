package com.idea.plugin.sql.support.enums;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.mysql.*;
import com.idea.plugin.sql.oracle.*;

import java.util.Arrays;

public enum DataProcedureTypeEnum {
    MYSQL_INITIAL(ProcedureTypeEnum.INITIAL, DataTypeEnum.MYSQL, new MysqlProcedureInitialService()),
    MYSQL_ADD_TABLE(ProcedureTypeEnum.ADD_TABLE, DataTypeEnum.MYSQL, new MysqlProcedureAddTableService()),
    MYSQL_ADD_INDEX(ProcedureTypeEnum.ADD_INDEX, DataTypeEnum.MYSQL, new MysqlProcedureAddIndexService()),
    MYSQL_ADD_COLUMN(ProcedureTypeEnum.ADD_COLUMN, DataTypeEnum.MYSQL, new MysqlProcedureAddColumnService()),
    MYSQL_ADD_DATA(ProcedureTypeEnum.ADD_DATA, DataTypeEnum.MYSQL, new MysqlProcedureAddDataService()),
    MYSQL_INSERT_SQL(ProcedureTypeEnum.INSERT_SQL, DataTypeEnum.MYSQL, new MysqlProcedureInsertSqlService()),
    ORACLE_INITIAL(ProcedureTypeEnum.INITIAL, DataTypeEnum.ORACLE, new OracleProcedureInitialService()),
    ORACLE_ADD_TABLE(ProcedureTypeEnum.ADD_TABLE, DataTypeEnum.ORACLE, new OracleProcedureAddTableService()),
    ORACLE_ADD_INDEX(ProcedureTypeEnum.ADD_INDEX, DataTypeEnum.ORACLE, new OracleProcedureAddIndexService()),
    ORACLE_ADD_COLUMN(ProcedureTypeEnum.ADD_COLUMN, DataTypeEnum.ORACLE, new OracleProcedureAddColumnService()),
    ORACLE_ADD_DATA(ProcedureTypeEnum.ADD_DATA, DataTypeEnum.ORACLE, new OracleProcedureAddDataService()),
    ORACLE_INSERT_DATA(ProcedureTypeEnum.INSERT_DATA, DataTypeEnum.ORACLE, new OracleProcedureInsertDataService()),
    ORACLE_INSERT_SQL(ProcedureTypeEnum.INSERT_SQL, DataTypeEnum.ORACLE, new OracleProcedureInsertSqlService()),
    ;


    private ProcedureTypeEnum procedureTypeEnum;
    private DataTypeEnum dataTypeEnum;
    private BaseProcedureService procedureService;

    DataProcedureTypeEnum(ProcedureTypeEnum procedureTypeEnum, DataTypeEnum dataTypeEnum, BaseProcedureService procedureService) {
        this.procedureTypeEnum = procedureTypeEnum;
        this.dataTypeEnum = dataTypeEnum;
        this.procedureService = procedureService;
    }

    public ProcedureTypeEnum getProcedureTypeEnum() {
        return procedureTypeEnum;
    }

    public DataTypeEnum getDataTypeEnum() {
        return dataTypeEnum;
    }

    public BaseProcedureService getProcedureService() {
        return procedureService;
    }

    public static BaseProcedureService getProcedureService(ProcedureTypeEnum procedureTypeEnum, DataTypeEnum dataTypeEnum) {
        return Arrays.stream(DataProcedureTypeEnum.values()).filter(dataProcedureTypeEnum ->
                        dataProcedureTypeEnum.getDataTypeEnum().equals(dataTypeEnum)
                                && dataProcedureTypeEnum.getProcedureTypeEnum().equals(procedureTypeEnum))
                .findAny().map(DataProcedureTypeEnum::getProcedureService).orElse(null);
    }
}
