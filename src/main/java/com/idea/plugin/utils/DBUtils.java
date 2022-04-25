package com.idea.plugin.utils;

import com.idea.plugin.sql.support.FieldInfoVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.DataTypeEnum;
import com.idea.plugin.sql.support.enums.FieldTypeEnum;
import com.idea.plugin.sql.support.enums.PrimaryTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class DBUtils {

    public static void getRowValues(List<String> valueList, DataTypeEnum dataTypeEnum, ResultSet resultSet, ResultSetMetaData metaData, List<String> declareColumns, List<String> dbmsLobCreates, List<String> dbmsLobApends) throws SQLException {
        for (int i = 2; i <= metaData.getColumnCount(); i++) {
            String columnLabel = metaData.getColumnLabel(i).toUpperCase();
            String columnClassName = metaData.getColumnClassName(i);
            Object value = resultSet.getString(i);
            if (value != null && value != "null") {
                if (String.class.getName().equals(columnClassName)) {
                    value = "'" + value + "'";
                } else if (Timestamp.class.getName().equals(columnClassName)
                        || LocalDate.class.getName().equals(columnClassName)
                        || LocalDateTime.class.getName().equals(columnClassName)
                        || Date.class.getName().equals(columnClassName)) {
                    Timestamp timestamp = resultSet.getTimestamp(i);
                    if (timestamp != null) {
                        if ("CREATE_DATE".equals(columnLabel) || "UPDATE_DATE".equals(columnLabel)) {
                            if (DataTypeEnum.MYSQL.equals(dataTypeEnum)) {
                                value = "SYSDATE()";
                            } else if (DataTypeEnum.ORACLE.equals(dataTypeEnum)) {
                                value = "SYSDATE";
                            }
                        } else {
                            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            value = "'" + dateFormat.format(timestamp.toLocalDateTime()) + "'";
                            if (DataTypeEnum.ORACLE.equals(dataTypeEnum)) {
                                value = "TIMESTAMP '" + dateFormat.format(timestamp.toLocalDateTime()) + "'";
                            }
                        }
                    }
                } else if (BigDecimal.class.getName().equals(columnClassName)) {
                    value = resultSet.getBigDecimal(i).doubleValue();
                } else if (Double.class.getName().equals(columnClassName)) {
                    value = resultSet.getDouble(i);
                } else if (Float.class.getName().equals(columnClassName)) {
                    value = resultSet.getFloat(i);
                } else if (Integer.class.getName().equals(columnClassName)) {
                    value = resultSet.getInt(i);
                }
            }
            String resultVlue = value == null ? "null" : value.toString();
            if (DataTypeEnum.ORACLE.equals(dataTypeEnum)) {
                if (resultVlue.length() > 3999) {
                    String declareColumnName = "V_" + columnLabel;
                    int separationLength = resultVlue.length() / 29999;
                    for (int j = 0; j <= separationLength; j++) {
                        if (j == separationLength) {
                            dbmsLobApends.add("    DBMS_LOB.APPEND(V_BO_TEMPLATE, '" + resultVlue.substring(j * 29999) + ");\n");
                        } else if (j == 0) {
                            dbmsLobApends.add("    DBMS_LOB.APPEND(V_BO_TEMPLATE, " + resultVlue.substring(0, (j + 1) * 29999) + "');\n");
                        } else {
                            dbmsLobApends.add("    DBMS_LOB.APPEND(V_BO_TEMPLATE, '" + resultVlue.substring(j * 29999, (j + 1) * 29999) + "');\n");
                        }
                    }
                    String declareColumn = "    " + declareColumnName + " CLOB;\n";
                    String dbmsLobCreate = "    DBMS_LOB.CREATETEMPORARY(" + declareColumnName + ", TRUE);\n";
                    declareColumns.add(declareColumn);
                    dbmsLobCreates.add(dbmsLobCreate);
                    resultVlue = declareColumnName;
                }
            }
            valueList.add(resultVlue);
        }
    }

    public static String getIdValue(String value) {
        if (StringUtils.isEmpty(value) || "UUID".equalsIgnoreCase(value)) {
            value = UUID.randomUUID().toString().replace("-", "");
        }
        return "'" + value + "'";
    }

    public static Connection getConnection(TableInfoVO tableInfoVO) {
        try {
            AssertUtils.assertIsTrue(StringUtils.isNotEmpty(tableInfoVO.username), "username不能为空");
            AssertUtils.assertIsTrue(StringUtils.isNotEmpty(tableInfoVO.password), "password不能为空");
            AssertUtils.assertIsTrue(StringUtils.isNotEmpty(tableInfoVO.jdbcUrl), "jdbcUrl不能为空");
            Properties properties = new Properties();
            properties.put("user", tableInfoVO.username);
            properties.put("password", tableInfoVO.password);
            properties.setProperty("remarks", "true");
            properties.put("useInformationSchema", "true");
            if (tableInfoVO.jdbcUrl.contains(DataTypeEnum.MYSQL.getCode())) {
                Class.forName("com.mysql.jdbc.Driver");
                return DriverManager.getConnection(tableInfoVO.jdbcUrl + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false", properties);
            } else if (tableInfoVO.jdbcUrl.contains(DataTypeEnum.ORACLE.getCode())) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                return DriverManager.getConnection(tableInfoVO.jdbcUrl, properties);
            }
            throw new SqlException("数据库配置错误");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public static void getTableInfoVOFromDB(TableInfoVO tableInfoVO) {
        getTableInfo(tableInfoVO);
    }

    private static void getTableInfo(TableInfoVO tableInfoVO) {
        Connection connection = getConnection(tableInfoVO);
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, "", tableInfoVO.tableName, new String[]{"TABLE"});
            if (tables.next()) {
                String remarks = tables.getString("REMARKS");
                if (StringUtils.isNotEmpty(remarks)) {
                    tableInfoVO.tableComment = remarks;
                }
            } else {
                throw new RuntimeException(String.format("数据库不存在表：%s", tableInfoVO.tableName));
            }
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableInfoVO.tableName);
            String primaryKey = null;
            while (primaryKeys.next()) {
                primaryKey = primaryKeys.getString("COLUMN_NAME");
            }
            ResultSet columns = metaData.getColumns(null, null, tableInfoVO.tableName, null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String remarks = columns.getString("REMARKS");
                FieldInfoVO fieldInfoVO = FieldInfoVO.builder()
                        .columnName(columnName)
                        .columnType(FieldTypeEnum.getFieldTypeBySqlType(columns.getInt("DATA_TYPE")));
                if (StringUtils.isNotEmpty(remarks)) {
                    fieldInfoVO.comment = remarks;
                }
                if (columnName.equals(primaryKey)) {
                    fieldInfoVO.primary = PrimaryTypeEnum.PRIMARY;
                }
                tableInfoVO.fieldInfos.add(fieldInfoVO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(connection);
        }
    }

    private static void getFieldInfo(TableInfoVO tableInfoVO) {
        Connection connection = getConnection(tableInfoVO);
        String sql = "SELECT * FROM";
        PreparedStatement preparedStatement = null;
        String tableSql = sql + tableInfoVO.tableName;
        try {
            preparedStatement = connection.prepareStatement(tableSql);
            ResultSetMetaData metaData = preparedStatement.getMetaData();
            int size = metaData.getColumnCount();
            for (int i = 0; i < size; i++) {
                FieldInfoVO fieldInfoVO = FieldInfoVO.builder()
                        .columnName(metaData.getColumnLabel(i).toUpperCase())
                        .columnType(FieldTypeEnum.getFieldTypeBySqlType(metaData.getColumnType(i)));
                tableInfoVO.fieldInfos.add(fieldInfoVO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(connection, preparedStatement);
            close(preparedStatement);
        }
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void close(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void close(Connection connection, PreparedStatement preparedStatement) {
        close(connection);
        close(preparedStatement);
    }

}
