package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.IProcedureService;

public class OracleProcedureAddIndex implements IProcedureService {

    public static String comment =
            "\n-- Oracle %s\n";

    public static String addIndexProcedure =
            "CREATE OR REPLACE PROCEDURE ADD_%s_INDEX(V_TABLE_NAME IN VARCHAR, V_INDEX_NAME IN VARCHAR, V_COLUMN_NAME IN VARCHAR) AS\n" +
                    "    V_T_COUNT NUMBER;\n" +
                    "BEGIN\n" +
                    "    SELECT count(1) INTO V_T_COUNT FROM USER_INDEXES WHERE TABLE_NAME = upper(V_TABLE_NAME) AND INDEX_NAME = upper(V_INDEX_NAME);\n" +
                    "    IF V_T_COUNT = 0 THEN EXECUTE IMMEDIATE 'CREATE INDEX ' || V_INDEX_NAME || ' ON ' || V_TABLE_NAME || ' (' || V_COLUMN_NAME || ')'; END IF;\n" +
                    "END ;\n" +
                    "/\n";
    public static String addIndexCall = "CALL ADD_%s_INDEX('%s', '%s', '%s');\n\n";
    public static String addIndexDrop = "DROP PROCEDURE ADD_%s_INDEX;\n\n";

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getProcedure() {
        return addIndexProcedure;
    }

    @Override
    public String getCall() {
        return addIndexCall;
    }

    @Override
    public String getDrop() {
        return addIndexDrop;
    }
}
