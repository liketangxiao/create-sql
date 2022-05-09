package com.idea.plugin.utils;

import com.idea.plugin.demo.DemoConfigVO;
import com.idea.plugin.sql.support.FieldInfoVO;
import com.idea.plugin.sql.support.IndexInfoVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.DataTypeEnum;
import com.idea.plugin.sql.support.enums.FieldTypeEnum;
import com.idea.plugin.sql.support.enums.NullTypeEnum;
import com.idea.plugin.sql.support.enums.PrimaryTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.*;

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

    public static Connection getConnection(String jdbcUrl, String username, String password) {
        TableInfoVO tableInfoVO = new TableInfoVO();
        tableInfoVO.jdbcUrl = jdbcUrl;
        tableInfoVO.username = username;
        tableInfoVO.password = password;
        return getConnection(tableInfoVO);
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
                int i = tableInfoVO.jdbcUrl.lastIndexOf("/");
                Connection connection = DriverManager.getConnection(tableInfoVO.jdbcUrl + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false", properties);
                tableInfoVO.schema = connection.getCatalog();
                tableInfoVO.dataType = DataTypeEnum.MYSQL.getCode();
                return connection;
            } else if (tableInfoVO.jdbcUrl.contains(DataTypeEnum.ORACLE.getCode())) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                tableInfoVO.schema = tableInfoVO.username.toUpperCase();
                tableInfoVO.dataType = DataTypeEnum.ORACLE.getCode();
                return DriverManager.getConnection(tableInfoVO.jdbcUrl, properties);
            }
            throw new SqlException("数据库配置错误" + tableInfoVO.jdbcUrl);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<String> getAllTableName(Connection connection, String schema) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(schema, schema, "%", new String[]{"TABLE"});
            List<String> ls = new ArrayList<>();
            while (rs.next()) {
                ls.add(rs.getString("TABLE_NAME"));
            }
            return ls;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(connection);
        }
    }


    public static void getTableInfoVOFromDB(TableInfoVO tableInfoVO) {
        getTableInfo(tableInfoVO);
    }

    public static void getTableInfo(TableInfoVO tableInfoVO) {
        Connection connection = null;
        try {
            connection = getConnection(tableInfoVO);
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(tableInfoVO.schema, tableInfoVO.schema, tableInfoVO.tableName, new String[]{"TABLE"});
            if (tables.next()) {
                String remarks = tables.getString("REMARKS");
                if (StringUtils.isNotEmpty(remarks)) {
                    tableInfoVO.tableComment = remarks;
                }
            } else {
                throw new RuntimeException(String.format("数据库不存在表：%s", tableInfoVO.tableName));
            }
            ResultSet primaryKeys = metaData.getPrimaryKeys(tableInfoVO.schema, tableInfoVO.schema, tableInfoVO.tableName);
            String primaryKey = "";
            while (primaryKeys.next()) {
                primaryKey = primaryKeys.getString("COLUMN_NAME");
            }
            ResultSet idxRs = metaData.getIndexInfo(tableInfoVO.schema, tableInfoVO.schema, tableInfoVO.tableName, false, false);
            while (idxRs.next()) {
                String indexName = idxRs.getString("INDEX_NAME");
                String columnName = idxRs.getString("COLUMN_NAME");
                if (StringUtils.isEmpty(columnName) || primaryKey.equals(columnName)) {
                    continue;
                }
                tableInfoVO.indexInfos(indexName, columnName);
            }
            ResultSet columns = metaData.getColumns(tableInfoVO.schema, tableInfoVO.schema, tableInfoVO.tableName, null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String remarks = columns.getString("REMARKS");
                String isNullable = columns.getString("IS_NULLABLE");
                String columnSize = columns.getString("COLUMN_SIZE");
                FieldTypeEnum dataType = null;
                if (tableInfoVO.jdbcUrl.contains(DataTypeEnum.MYSQL.getCode())) {
                    dataType = FieldTypeEnum.getFieldTypeBySqlType(columns.getInt("DATA_TYPE"));
                } else if (tableInfoVO.jdbcUrl.contains(DataTypeEnum.ORACLE.getCode())) {
                    String typeName = columns.getString("TYPE_NAME");
                    if (typeName.contains("(")) {
                        typeName = typeName.replaceAll("\\(.*\\)", "");
                    }
                    dataType = FieldTypeEnum.getFieldTypeByOType(typeName);
                }
                if (dataType == null) {
                    dataType = FieldTypeEnum.VARCHAR;
                }
                int digits = columns.getInt("DECIMAL_DIGITS");
                FieldInfoVO fieldInfoVO = FieldInfoVO.builder()
                        .columnName(columnName)
                        .columnType(dataType);
                if (StringUtils.isNotEmpty(remarks)) {
                    fieldInfoVO.comment = remarks;
                }
                if (columnName.equals(primaryKey)) {
                    fieldInfoVO.primary = PrimaryTypeEnum.PRIMARY;
                }
                if (!isNullable.equals("YES")) {
                    fieldInfoVO.nullType = NullTypeEnum.NOT_NULL;
                }
                if (FieldTypeEnum.VARCHAR.equals(fieldInfoVO.columnType)) {
                    fieldInfoVO.columnTypeArgs = columnSize;
                }
                if (FieldTypeEnum.NUMBER.equals(fieldInfoVO.columnType)) {
                    fieldInfoVO.columnTypeArgs = columnSize + "," + digits;
                }
                tableInfoVO.fieldInfos.add(fieldInfoVO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(connection);
        }
    }

    public static void getTableDataInfo(TableInfoVO tableInfoVO) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection(tableInfoVO);
            String sql = "SELECT * FROM " + tableInfoVO.tableName;
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> codeList = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            String idCode = metaData.getColumnLabel(1);
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                codeList.add(metaData.getColumnLabel(i).toUpperCase());
            }
            String codes = String.join(", ", codeList);
            String idValue = "";
            if (resultSet.next()) {
                List<String> rowValues = new ArrayList<>();
                idValue = DBUtils.getIdValue(resultSet.getString(1));
                rowValues.add(idValue);
                DBUtils.getRowValues(rowValues, DataTypeEnum.MYSQL, resultSet, metaData, null, null, null);
                String values = String.join(", ", rowValues);
                tableInfoVO.insertData("INSERT INTO " + tableInfoVO.tableName + " (" + codes + ") VALUES (" + values + ");");
            }
            tableInfoVO.insertSql = "SELECT * FROM " + tableInfoVO.tableName + " WHERE " + idCode + " = " + idValue + ";";
            tableInfoVO.insertColumnName(codes, idCode);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(connection, preparedStatement);
        }
    }

    public static TableInfoVO initTableInfoVO(DemoConfigVO demoConfigVO) {
        TableInfoVO tableInfoVO = TableInfoVO.builder()
                .jdbcUrl(demoConfigVO.jdbcUrl)
                .username(demoConfigVO.username)
                .password(demoConfigVO.password)
                .tableInfo("T_TABLE_NAME", "示例表");
        tableInfoVO.indexInfos.add(IndexInfoVO.builder().indexColumnName("COL_NAME, CREATE_DATE").indexName("INDEX_T_NAME"));
        tableInfoVO.fieldInfos.add(FieldInfoVO.builder().columnName("ID").columnType(FieldTypeEnum.VARCHAR)
                .comment("主键").primary(PrimaryTypeEnum.PRIMARY));
        tableInfoVO.fieldInfos.add(FieldInfoVO.builder().columnName("COL_NAME").columnType(FieldTypeEnum.VARCHAR)
                .comment("字段注释"));
        tableInfoVO.fieldInfos.add(FieldInfoVO.builder().columnName("CREATE_DATE").columnType(FieldTypeEnum.TIMESTAMP)
                .comment("创建时间"));
        tableInfoVO.insertSql = "SELECT * FROM " + tableInfoVO.tableName + " WHERE ID = '';";
        tableInfoVO.insertColumnName("ID, COL_NAME, CREATE_DATE", "ID");
        tableInfoVO.insertData("INSERT INTO " + tableInfoVO.tableName + " (ID, COL_NAME, CREATE_DATE) VALUES ('0279a59a333da6ee68dd7b3000400001', 'name', '2022-03-23 16:07:57');");
        tableInfoVO.insertData("INSERT INTO " + tableInfoVO.tableName + " (ID, COL_NAME, CREATE_DATE) VALUES ('0279a59a333da6ee68dd7b3000400001', 'name', '2022-03-23 16:07:57');");
        return tableInfoVO;
    }

    public static void addTableInfoAttri(TableInfoVO tableInfoVO) {

        if (CollectionUtils.isEmpty(tableInfoVO.indexInfos)) {
            tableInfoVO.indexInfos.add(IndexInfoVO.builder().indexColumnName("COL_NAME, CREATE_DATE").indexName("INDEX_T_NAME"));
        }
        if (CollectionUtils.isEmpty(tableInfoVO.fieldInfos)) {
            tableInfoVO.fieldInfos.add(FieldInfoVO.builder().columnName("ID").columnType(FieldTypeEnum.VARCHAR)
                    .comment("主键").primary(PrimaryTypeEnum.PRIMARY));
            tableInfoVO.fieldInfos.add(FieldInfoVO.builder().columnName("COL_NAME").columnType(FieldTypeEnum.VARCHAR)
                    .comment("字段注释"));
            tableInfoVO.fieldInfos.add(FieldInfoVO.builder().columnName("CREATE_DATE").columnType(FieldTypeEnum.TIMESTAMP)
                    .comment("创建时间"));
        }
        if (StringUtils.isEmpty(tableInfoVO.insertSql)) {
            tableInfoVO.insertSql = "SELECT * FROM " + tableInfoVO.tableName + " WHERE ID = '';";
        }
        if (StringUtils.isEmpty(tableInfoVO.insertColumnName)) {
            tableInfoVO.insertColumnName("ID, COL_NAME, CREATE_DATE", "ID");
        }
        if (StringUtils.isEmpty(tableInfoVO.insertData)) {
            tableInfoVO.insertData("INSERT INTO " + tableInfoVO.tableName + " (ID, COL_NAME, CREATE_DATE) VALUES ('0279a59a333da6ee68dd7b3000400001', 'name', '2022-03-23 16:07:57');");
            tableInfoVO.insertData("INSERT INTO " + tableInfoVO.tableName + " (ID, COL_NAME, CREATE_DATE) VALUES ('0279a59a333da6ee68dd7b3000400001', 'name', '2022-03-23 16:07:57');");
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
