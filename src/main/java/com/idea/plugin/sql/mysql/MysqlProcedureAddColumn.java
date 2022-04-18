package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.IProcedureService;

public class MysqlProcedureAddColumn implements IProcedureService {

    public static String comment =
            "\n-- Mysql %s\n";

    public static String addColumnProcedure =
            "DROP PROCEDURE IF EXISTS ADD_%s_COLUMN;\n" +
                    "DELIMITER $$\n" +
                    "CREATE PROCEDURE ADD_%s_COLUMN(V_TABLE_NAME TINYTEXT, V_COLUMN_NAME TINYTEXT, V_COLUMN_TYPE TINYTEXT, V_COMMENT TINYTEXT)\n" +
                    "BEGIN\n" +
                    "    BEGIN\n" +
                    "        SET @STR = concat('ALTER TABLE ', V_TABLE_NAME, '  ADD ', V_COLUMN_NAME, ' ', V_COLUMN_TYPE, ' COMMENT ''', V_COMMENT, '''');\n" +
                    "        SELECT count(1) INTO @CNT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = V_TABLE_NAME AND COLUMN_NAME = V_COLUMN_NAME AND TABLE_SCHEMA = DATABASE();\n" +
                    "        IF @CNT = 0 THEN PREPARE STMT FROM @STR; EXECUTE STMT; END IF;\n" +
                    "    END;\n" +
                    "END$$\n" +
                    "DELIMITER ;\n" +
                    "\n";
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
