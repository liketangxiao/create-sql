package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.IProcedureService;

public class OracleProcedureInsertData implements IProcedureService {

    public static String comment =
            "\n-- Oracle %s\n";

    public static String insertDataProcedure =
            "INSERT INTO %s(%s)\n" +
                    "SELECT %s FROM DUAL\n" +
                    "WHERE NOT exists(SELECT 1 FROM %s T WHERE T.%s = %s);\n";

    public static String insertDataCall =
            "DECLARE\n" +
                    "%s" +
                    "BEGIN\n" +
                    "%s" +
                    "%s" +
                    "    INSERT INTO %s(%s)\n" +
                    "    SELECT %s FROM DUAL\n" +
                    "    WHERE NOT exists(SELECT 1 FROM %s T WHERE T.%s = %s);\n" +
                    "END ;\n" +
                    "/\n";

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
        return insertDataCall;
    }

    @Override
    public String getDrop() {
        return null;
    }
}
