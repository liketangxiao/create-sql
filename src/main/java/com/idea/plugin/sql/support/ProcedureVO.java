package com.idea.plugin.sql.support;


import com.idea.plugin.sql.support.enums.ProcedureTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProcedureVO {
    public String author;
    public String filePath;
    public String fileName;
    public String jdbcUrl;
    public String username;
    public String password;
    public List<TableInfoVO> tableInfoVOS = new ArrayList<>();

    public static ProcedureVO builder() {
        ProcedureVO procedureVO = new ProcedureVO();
        TableInfoVO tableInfoVO = TableInfoVO.builder();
        tableInfoVO.getProcedureType().add(ProcedureTypeEnum.INITIAL);
        procedureVO.getTableInfoVOS().add(tableInfoVO);
        return procedureVO;
    }



    public String getAuthor() {
        return author;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public List<TableInfoVO> getTableInfoVOS() {
        return tableInfoVOS;
    }

    public ProcedureVO author(String author) {
        this.author = author;
        return this;
    }

    public ProcedureVO filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public ProcedureVO fileName(String fileName) {
        this.fileName = fileName;
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
