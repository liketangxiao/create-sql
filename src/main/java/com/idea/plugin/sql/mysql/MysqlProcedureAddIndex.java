package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.IProcedureService;

public class MysqlProcedureAddIndex implements IProcedureService {

    public static String comment =
            "\n-- Mysql %s\n";

    public static String addIndexProcedure =
            "DROP PROCEDURE IF EXISTS ADD_%s_INDEX;\n" +
                    "DELIMITER $$\n" +
                    "CREATE PROCEDURE ADD_%s_INDEX(V_TABLE_NAME TINYTEXT, V_INDEX_NAME TINYTEXT, V_COLUMN_NAME TINYTEXT)\n" +
                    "BEGIN\n" +
                    "    BEGIN\n" +
                    "        SET @STR = concat('CREATE INDEX ', V_INDEX_NAME, ' ON ', V_TABLE_NAME, ' (', V_COLUMN_NAME, ')');\n" +
                    "        SELECT count(1) INTO @CNT FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = V_TABLE_NAME AND INDEX_NAME = V_INDEX_NAME AND TABLE_SCHEMA = DATABASE();\n" +
                    "        IF @CNT = 0 THEN PREPARE STMT FROM @STR; EXECUTE STMT; END IF;\n" +
                    "    END;\n" +
                    "END$$\n" +
                    "DELIMITER ;\n" +
                    "\n";
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
