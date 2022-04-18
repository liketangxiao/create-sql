package com.idea.plugin.sql.utils;

import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.DataTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AssertUtils {

    public static void assertIsTrue(Boolean flag, String message) throws SqlException {
        if (!flag) {
            throw new SqlException(message);
        }
    }

}
