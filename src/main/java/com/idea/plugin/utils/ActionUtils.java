package com.idea.plugin.utils;

import com.idea.plugin.demo.DemoConfigVO;
import com.idea.plugin.demo.DemoSettings;
import com.idea.plugin.sql.support.ProcedureVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ActionUtils {
    public static ProcedureVO readProcedureByText(String text) {
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
                property = property.replaceAll("，", ",");
                property = property.replaceAll("；", ";");
                property = property.replaceFirst("：", ":");
                if (property.startsWith("procedureType")) {
                    if (StringUtils.isNotEmpty(insertSql)) {
                        String sql = insertSql.toString().replace(";", "");
                        insertSql = new StringBuilder();
                        Method tableInfoMethod = tableInfoMethodMap.get("insertSql");
                        tableInfoMethod.invoke(tableInfoVO, sql);
                    }
                    tableInfoVO = TableInfoVO.builder();
                    tableInfoVO.procedureVO(procedureVO);
                    procedureVO.tableInfoVOS(tableInfoVO);
                }
                if (tableInfoVO != null && (property.toUpperCase().startsWith("INSERT INTO"))) {
                    tableInfoVO.insertData(property);
                } else {
                    int index = property.indexOf(":");
                    if (property.startsWith("insertSql") || insertSql.length() > 0) {
                        insertSql.append(property.substring(index + 1)).append(" ");
                        String sql = insertSql.toString().trim();
                        Method tableInfoMethod = tableInfoMethodMap.get("insertSql");
                        tableInfoMethod.invoke(tableInfoVO, sql);
                        if (sql.endsWith(";")) {
                            sql = insertSql.toString().replace(";", "");
                            insertSql = new StringBuilder();
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
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                Messages.showErrorDialog("配置文件转换失败: " + methodName + " " + ((InvocationTargetException) e).getTargetException().getLocalizedMessage(), "错误");
            }
            Messages.showErrorDialog("配置文件转换失败: " + methodName + " " + e.getLocalizedMessage(), "错误");
        }
        return procedureVO;
    }

    public static void readDemoConfigByText(String text) {
        DemoSettings demoSettings = DemoSettings.getInstance(ProjectManager.getInstance().getDefaultProject());
        if (demoSettings.getDemoConfigVO() == null) {
            return;
        }
        String[] properties = text.split("\n");
        DemoConfigVO config = demoSettings.getDemoConfigVO();
        config.tableName = null;
        Class<DemoConfigVO> configClass = DemoConfigVO.class;
        try {
            Method[] configClassDeclaredMethods = configClass.getDeclaredMethods();
            Map<String, Method> configMethodMap = Arrays.stream(configClassDeclaredMethods).collect(Collectors.toMap(Method::getName, Function.identity()));
            for (int i = 0; i < properties.length && i < 8; i++) {
                String property = properties[i];
                property = property.trim();
                if (StringUtils.isEmpty(property) || property.startsWith("#") || property.startsWith("--") || !property.contains(":")) {
                    continue;
                }
                int index = property.indexOf(":");
                String methodName = property.substring(0, index).trim();
                Object[] args = Arrays.stream(property.substring(index + 1).split(";")).map(String::trim).toArray();
                if (args.length == 0) {
                    continue;
                }
                methodName = "set" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                Method procedureMethod = configMethodMap.get(methodName);
                if (procedureMethod != null) {
                    procedureMethod.invoke(config, args);
                }
            }
        } catch (Exception ignored) {
        }
    }
}
