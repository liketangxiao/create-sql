package com.idea.plugin.demo;

import com.idea.plugin.sql.support.TableInfoVO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DemoConfigVO {
    public String author;
    public String filePath;
    public String fileName;
    public String modulePath;
    public String jdbcUrl;
    public String username;
    public String password;
    public String tableName;
    private List<String> procedureTypeList = new ArrayList<>();
    private List<TableInfoVO> tableInfoVOS = new ArrayList<>();

    public Map<String, List<String>> tabNameCacheMap = new ConcurrentHashMap<>();
    public Map<String, TableInfoVO> tableInfoCacheMap = new ConcurrentHashMap<>();

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getProcedureTypeList() {
        return procedureTypeList;
    }

    public void setProcedureTypeList(List<String> procedureTypeList) {
        this.procedureTypeList = procedureTypeList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getTableNameList() {
        if (StringUtils.isNotEmpty(tableName)) {
            return Arrays.stream(tableName.split(";")).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<TableInfoVO> getTableInfoVOS() {
        return tableInfoVOS;
    }

    public void setTableInfoVOS(List<TableInfoVO> tableInfoVOS) {
        this.tableInfoVOS = tableInfoVOS;
    }
}
