package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.IProcedureService;

public class MysqlProcedureAddTable implements IProcedureService {

    public static String comment =
            "\n-- Mysql %s创建表\n";

    public static String addTableProcedure =
            "CREATE TABLE IF NOT EXISTS %s\n" +
                    "(\n" +
                    "%s\n" +
                    ") COMMENT '%s';\n" +
                    "\n";
    public static String addTableCall = "    %s %s %s COMMENT '%s'";

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getProcedure() {
        return addTableProcedure;
    }

    @Override
    public String getCall() {
        return addTableCall;
    }

    @Override
    public String getDrop() {
        return null;
    }
}
