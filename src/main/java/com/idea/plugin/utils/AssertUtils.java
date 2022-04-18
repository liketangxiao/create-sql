package com.idea.plugin.utils;

import com.idea.plugin.sql.support.exception.SqlException;

public class AssertUtils {

    public static void assertIsTrue(Boolean flag, String message) throws SqlException {
        if (!flag) {
            throw new SqlException(message);
        }
    }

}
