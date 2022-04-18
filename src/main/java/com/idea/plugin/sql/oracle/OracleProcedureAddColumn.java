package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.IProcedureService;

public class OracleProcedureAddColumn implements IProcedureService {

    public static String comment =
            "\n-- Oracle %s创建字段\n";

    public static String addColumnProcedure =
            "CREATE OR REPLACE PROCEDURE ADD_%s_COLUMN(V_TABLE_NAME IN VARCHAR, V_COLUMN_NAME IN VARCHAR, V_COLUMN_TYPE IN VARCHAR, V_COMMENT IN VARCHAR) AS\n" +
                    "    V_T_COUNT NUMBER;\n" +
                    "BEGIN\n" +
                    "    SELECT count(1) INTO V_T_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = upper(V_TABLE_NAME) AND COLUMN_NAME = upper(V_COLUMN_NAME);\n" +
                    "    IF V_T_COUNT = 0 THEN\n" +
                    "        EXECUTE IMMEDIATE 'ALTER TABLE ' || V_TABLE_NAME || ' ADD ' || V_COLUMN_NAME || ' ' || V_COLUMN_TYPE;\n" +
                    "        EXECUTE IMMEDIATE 'COMMENT ON COLUMN ' || V_TABLE_NAME || '.' || V_COLUMN_NAME || ' IS ''' || V_COMMENT || '''';\n" +
                    "    END IF;\n" +
                    "END ;\n" +
                    "/\n";
    public static String addColumnCall = "CALL ADD_%s_COLUMN('%s', '%s', '%s %s', '%s');\n\n";
    public static String addColumnDrop = "DROP PROCEDURE ADD_%s_COLUMN;\n\n";

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getProcedure() {
        return addColumnProcedure;
    }

    @Override
    public String getCall() {
        return addColumnCall;
    }

    @Override
    public String getDrop() {
        return addColumnDrop;
    }
}
