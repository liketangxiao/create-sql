package com.idea.plugin.sql.support;


import java.util.ArrayList;
import java.util.List;

public class ProcedureVO {
    public String author;
    public String filePath;
    public String fileName;
    public String modulePath;
    public String jdbcUrl;
    public String username;
    public String password;
    public List<TableInfoVO> tableInfoVOS = new ArrayList<>();

    public static ProcedureVO builder() {
        return new ProcedureVO();
    }

    public ProcedureVO author(String author) {
        this.author = author;
        return this;
    }

    public ProcedureVO filePath(String filePath) {
        this.filePath = filePath.replaceAll("\\\\","/");
        return this;
    }

    public ProcedureVO fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ProcedureVO modulePath(String modulePath) {
        this.modulePath = modulePath.replaceAll("\\\\","/");
        return this;
    }

    public ProcedureVO jdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    public ProcedureVO username(String username) {
        this.username = username;
        return this;
    }

    public ProcedureVO password(String password) {
        this.password = password;
        return this;
    }

    public ProcedureVO tableInfoVOS(TableInfoVO tableInfoVO) {
        this.tableInfoVOS.add(tableInfoVO);
        return this;
    }
}
