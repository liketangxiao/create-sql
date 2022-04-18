package com.idea.plugin.sql;

import com.idea.plugin.sql.support.ProcedureVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.DataProcedureTypeEnum;
import com.idea.plugin.sql.support.enums.DataTypeEnum;
import com.idea.plugin.sql.support.enums.FileTypeEnum;
import com.idea.plugin.sql.support.enums.ProcedureTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import com.idea.plugin.utils.AssertUtils;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CreateSqlFile {

    public void createSqlFileByText(String text) {
        String[] properties = text.split("\n");
        ProcedureVO procedureVO = ProcedureVO.builder();
        TableInfoVO tableInfoVO = null;
        Class<ProcedureVO> procedureVOClass = ProcedureVO.class;
        Class<TableInfoVO> tableInfoVOClass = TableInfoVO.class;
        String methodName = null;
        try {
            Method[] procedureVOClassDeclaredMethods = procedureVOClass.getDeclaredMethods();
            Method[] tableInfoVOClassDeclaredMethods = tableInfoVOClass.getDeclaredMethods();
            Map<String, Method> procedureMethodMap = Arrays.stream(procedureVOClassDeclaredMethods).collect(Collectors.toMap(Method::getName, Function.identity()));
            Map<String, Method> tableInfoMethodMap = Arrays.stream(tableInfoVOClassDeclaredMethods).collect(Collectors.toMap(Method::getName, Function.identity()));
            StringBuilder insertSql = new StringBuilder();
            for (String property : properties) {
                property = property.trim();
                if (StringUtils.isEmpty(property) || property.startsWith("#") || property.startsWith("--")) {
                    continue;
                }
                if (property.startsWith("procedureType")) {
                    tableInfoVO = TableInfoVO.builder();
                    procedureVO.tableInfoVOS(tableInfoVO);
                }
                if (tableInfoVO != null && (property.toUpperCase().startsWith("INSERT INTO"))) {
                    tableInfoVO.insertData(property);
                } else {
                    int index = property.indexOf(":");
                    if (property.startsWith("insertSql") || insertSql.length() > 0) {
                        insertSql.append(property.substring(index + 1)).append(" ");
                        String sql = insertSql.toString().trim();
                        if (sql.endsWith(";")) {
                            sql = insertSql.toString().replace(";", "");
                            insertSql = new StringBuilder();
                            Method tableInfoMethod = tableInfoMethodMap.get("insertSql");
                            tableInfoMethod.invoke(tableInfoVO, sql);
                        }
                    } else {
                        methodName = property.substring(0, index).trim();
                        Method tableInfoMethod = tableInfoMethodMap.get(methodName);
                        Object[] args = Arrays.stream(property.substring(index + 1).split(";")).map(String::trim).toArray();
                        if (args.length == 0) {
                            continue;
                        }
                        if (tableInfoVO != null && tableInfoMethod != null) {
                            if (args.length < tableInfoMethod.getParameterCount()) {
                                Object[] argsCopy = new Object[tableInfoMethod.getParameterCount()];
                                for (int i = 0; i < tableInfoMethod.getParameterCount(); i++) {
                                    if (i < args.length) {
                                        argsCopy[i] = args[i];
                                    } else {
                                        argsCopy[i] = null;
                                    }
                                }
                                tableInfoMethod.invoke(tableInfoVO, argsCopy);
                            } else {
                                tableInfoMethod.invoke(tableInfoVO, args);
                            }
                        } else {
                            Method procedureMethod = procedureMethodMap.get(methodName);
                            if (procedureMethod != null) {
                                procedureMethod.invoke(procedureVO, args);
                            }
                        }
                    }
                }
            }
            try {
                createSqlFile(procedureVO);
                Messages.showMessageDialog("sql文件创建成功: " + procedureVO.fileName, "正确", Messages.getInformationIcon());
            } catch (Exception ex) {
                Messages.showErrorDialog("sql文件创建失败: " + ex.getLocalizedMessage(), "错误");
            }
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                Messages.showErrorDialog("配置文件转换失败: " + methodName + " " + ((InvocationTargetException) e).getTargetException().getLocalizedMessage(), "错误");
            }
            Messages.showErrorDialog("配置文件转换失败: " + methodName + " " + e.getLocalizedMessage(), "错误");
        }

    }

    public void createSqlFile(ProcedureVO procedureVO) throws SqlException {
        AssertUtils.assertIsTrue(StringUtils.isNotEmpty(procedureVO.filePath), "文件路径不能为空");
        List<FileTypeEnum> fileTypeEnums = procedureVO.tableInfoVOS.stream().flatMap(tableInfoVO -> tableInfoVO.procedureType.stream()).map(ProcedureTypeEnum::getFileType).distinct().collect(Collectors.toList());
        FileTypeEnum fileType = FileTypeEnum.getFirstFileType(fileTypeEnums);
        LocalDateTime localDateTime = LocalDateTime.now();
        String mysqlPath = getFilePath(DataTypeEnum.MYSQL, fileType, procedureVO.filePath, procedureVO.fileName, localDateTime);
        String oralcePath = getFilePath(DataTypeEnum.ORACLE, fileType, procedureVO.filePath, procedureVO.fileName, localDateTime);
        for (TableInfoVO tableInfoVO : procedureVO.tableInfoVOS) {
            tableInfoVO.procedureVO(procedureVO);
            for (ProcedureTypeEnum procedureTypeEnum : tableInfoVO.procedureType) {
                if (procedureTypeEnum != null) {
                    BaseProcedureService mysqlProcedureService = DataProcedureTypeEnum.getProcedureService(procedureTypeEnum, DataTypeEnum.MYSQL);
                    BaseProcedureService oralceProcedureService = DataProcedureTypeEnum.getProcedureService(procedureTypeEnum, DataTypeEnum.ORACLE);
                    if (mysqlProcedureService != null) {
                        mysqlProcedureService.addProcedure(mysqlPath, tableInfoVO);
                    }
                    if (oralceProcedureService != null) {
                        oralceProcedureService.addProcedure(oralcePath, tableInfoVO);
                    }
                }
            }
        }
    }


    public String getFilePath(DataTypeEnum dataType, FileTypeEnum fileType, String filePath, String fileName, LocalDateTime localDateTime) throws SqlException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");
        File file = new File(filePath);
        filePath = filePath + "/" + dataType.getCode();
        if (!file.exists() && !file.isDirectory()) {
            throw new SqlException(String.format("文件%s不存在", filePath));
        } else {
            file = new File(filePath);
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
        }
        return filePath + "/V" + dateFormat.format(localDateTime) + "__" + fileType.getCode() + "." + fileName + "_" + dataType.getCode() + ".sql";
    }

}
