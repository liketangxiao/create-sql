package com.idea.plugin.sql.support;


import com.idea.plugin.sql.support.enums.ProcedureTypeEnum;

import java.util.ArrayList;
import java.util.List;

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
        tableInfoVO.procedureType.add(ProcedureTypeEnum.INITIAL);
        procedureVO.tableInfoVOS.add(tableInfoVO);
        return procedureVO;
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
