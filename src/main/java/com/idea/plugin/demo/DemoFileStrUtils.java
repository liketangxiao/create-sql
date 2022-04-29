package com.idea.plugin.demo;

import com.idea.plugin.sql.support.FieldInfoVO;
import com.idea.plugin.sql.support.IndexInfoVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.NullTypeEnum;
import com.idea.plugin.sql.support.enums.PrimaryTypeEnum;
import com.idea.plugin.sql.support.enums.ProcedureTypeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class DemoFileStrUtils {
    public static String sqlFileStr(DemoConfigVO configVO) {
        StringBuilder sqlFileStr = new StringBuilder("-- 创建sql文件配置参数\n" +
                "author:" + configVO.author + "\n" +
                "filePath:" + configVO.filePath + "\n" +
                "fileName:" + configVO.fileName + "\n" +
                "jdbcUrl:" + configVO.jdbcUrl + "\n" +
                "username:" + configVO.username + "\n" +
                "password:" + configVO.password + "\n\n");
        for (TableInfoVO tableInfoVO : configVO.getTableInfoVOS()) {
            if (configVO.getProcedureTypeList().contains("ADD_TABLE")) {
                StringBuilder tableInfoStr = new StringBuilder("-- 示例ADD_TABLE\n" +
                        "procedureType:ADD_TABLE\n" +
                        "comment:" + tableInfoVO.tableComment + "(" + tableInfoVO.tableName + ")内置\n" +
                        "tableInfo:" + tableInfoVO.tableName + "; " + tableInfoVO.tableComment + "\n");
                for (FieldInfoVO fieldInfo : tableInfoVO.fieldInfos) {
                    String args = null;
                    if (PrimaryTypeEnum.PRIMARY.equals(fieldInfo.primary)) {
                        args = PrimaryTypeEnum.PRIMARY.name();
                    } else if (NullTypeEnum.NOT_NULL.equals(fieldInfo.nullType)) {
                        args = NullTypeEnum.NOT_NULL.name();
                    }
                    if (args != null) {
                        tableInfoStr.append("fieldInfos:").append(fieldInfo.columnName).append("; ").append(fieldInfo.columnType.getType(fieldInfo.columnTypeArgs)).append("; ").append(fieldInfo.comment).append("; ").append(args).append("\n");
                    } else {
                        tableInfoStr.append("fieldInfos:").append(fieldInfo.columnName).append("; ").append(fieldInfo.columnType.getType(fieldInfo.columnTypeArgs)).append("; ").append(fieldInfo.comment).append("\n");
                    }
                }
                sqlFileStr.append(tableInfoStr).append("\n");
            }
            if (configVO.getProcedureTypeList().contains("ADD_INDEX")) {
                StringBuilder tableInfoStr = new StringBuilder("-- 示例ADD_INDEX\n" +
                        "procedureType:ADD_INDEX\n" +
                        "comment:" + tableInfoVO.tableComment + "(" + tableInfoVO.tableName + ")新增索引\n" +
                        "tableInfo:" + tableInfoVO.tableName + "; " + tableInfoVO.tableComment + "\n");
                for (IndexInfoVO indexInfo : tableInfoVO.indexInfos) {
                    tableInfoStr.append("indexInfos:").append(indexInfo.indexName).append("; ").append(indexInfo.indexColumnName).append("\n");
                }
                sqlFileStr.append(tableInfoStr).append("\n");
            }
            if (configVO.getProcedureTypeList().contains("ADD_COLUMN")) {
                StringBuilder tableInfoStr = new StringBuilder("-- 示例ADD_COLUMN\n" +
                        "procedureType:ADD_COLUMN\n" +
                        "comment:" + tableInfoVO.tableComment + "(" + tableInfoVO.tableName + ")新增字段\n" +
                        "tableInfo:" + tableInfoVO.tableName + "; " + tableInfoVO.tableComment + "\n");
                for (int i = 1; i < tableInfoVO.fieldInfos.size() && i < 3; i++) {
                    FieldInfoVO fieldInfo = tableInfoVO.fieldInfos.get(i);
                    String args = null;
                    if (PrimaryTypeEnum.PRIMARY.equals(fieldInfo.primary)) {
                        args = PrimaryTypeEnum.PRIMARY.name();
                    } else if (NullTypeEnum.NOT_NULL.equals(fieldInfo.nullType)) {
                        args = NullTypeEnum.NOT_NULL.name();
                    }
                    if (args != null) {
                        tableInfoStr.append("fieldInfos:").append(fieldInfo.columnName).append("; ").append(fieldInfo.columnType.getType(fieldInfo.columnTypeArgs)).append("; ").append(fieldInfo.comment).append("; ").append(args).append("\n");
                        ;
                    } else {
                        tableInfoStr.append("fieldInfos:").append(fieldInfo.columnName).append("; ").append(fieldInfo.columnType.getType(fieldInfo.columnTypeArgs)).append("; ").append(fieldInfo.comment).append("\n");
                    }
                }
                sqlFileStr.append(tableInfoStr).append("\n");

            }
            if (configVO.getProcedureTypeList().contains("INSERT_DATA")) {
                String tableInfoStr = "-- 示例INSERT_DATA\n" +
                        "procedureType:INSERT_DATA\n" +
                        "comment:" + tableInfoVO.tableComment + "(" + tableInfoVO.tableName + ")新增数据\n" +
                        "tableInfo:" + tableInfoVO.tableName + "; " + tableInfoVO.tableComment + "\n" +
                        tableInfoVO.insertData + "\n";
                sqlFileStr.append(tableInfoStr).append("\n");
            }
            if (configVO.getProcedureTypeList().contains("INSERT_SQL")) {
                String tableInfoStr = "-- 示例INSERT_SQL\n" +
                        "procedureType:INSERT_SQL\n" +
                        "comment:" + tableInfoVO.tableComment + "(" + tableInfoVO.tableName + ")新增数据\n" +
                        "tableInfo:" + tableInfoVO.tableName + "; " + tableInfoVO.tableComment + "\n" +
                        "insertSql:" + tableInfoVO.insertSql + "\n\n";
                sqlFileStr.append(tableInfoStr).append("\n");

            }
            if (configVO.getProcedureTypeList().contains("ADD_DATA")) {
                String tableInfoStr = "-- 示例ADD_DATA\n" +
                        "procedureType:ADD_DATA\n" +
                        "comment:" + tableInfoVO.tableComment + "(" + tableInfoVO.tableName + ")新增数据\n" +
                        "tableInfo:" + tableInfoVO.tableName + "; " + tableInfoVO.tableComment + "\n" +
                        "insertColumnName:" + tableInfoVO.insertColumnName + "; " + tableInfoVO.insertColumnParam + "\n\n";
                sqlFileStr.append(tableInfoStr).append("\n");
            }
        }
        return sqlFileStr.toString();
    }

    public static String javaFileStr(DemoConfigVO configVO) {
        StringBuilder javaFileStr = new StringBuilder("-- 创建java文件配置参数\n" +
                "author:" + configVO.author + "\n" +
                "modulePath:" + configVO.modulePath + "\n" +
                "jdbcUrl:" + configVO.jdbcUrl + "\n" +
                "username:" + configVO.username + "\n" +
                "password:" + configVO.password + "\n\n");
        for (TableInfoVO tableInfoVO : configVO.getTableInfoVOS()) {
            List<String> fileCreateTypes = Arrays.stream(ProcedureTypeEnum.values()).filter(procedureTypeEnum -> procedureTypeEnum.getFileCreateType() != null)
                    .map(Enum::name).collect(Collectors.toList());
            String fileCreateType = configVO.getProcedureTypeList().stream().filter(fileCreateTypes::contains).collect(Collectors.joining(", "));
            String tableInfoStr = "-- 示例" + fileCreateType + "\n" +
                    "procedureType:" + fileCreateType + "\n" +
                    "tableInfo:" + tableInfoVO.tableName + "\n";
            javaFileStr.append(tableInfoStr).append("\n");
        }
        return javaFileStr.toString();
    }

}
