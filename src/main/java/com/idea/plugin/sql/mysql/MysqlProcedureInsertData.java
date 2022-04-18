package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.IProcedureService;

public class MysqlProcedureInsertData implements IProcedureService {

    public static String comment =
            "\n-- Mysql %s数据重复插入\n";

    public static String insertDataProcedure =
            "INSERT INTO %s(%s)\n" +
                    "SELECT %s FROM DUAL\n" +
                    "WHERE NOT exists(SELECT 1 FROM %s T WHERE T.%s = %s);\n";

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getProcedure() {
        return insertDataProcedure;
    }

    @Override
    public String getCall() {
        return null;
    }

    @Override
    public String getDrop() {
        return null;
    }
}
