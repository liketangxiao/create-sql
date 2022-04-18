package com.idea.plugin.sql.support;


import com.idea.plugin.sql.support.enums.FieldTypeEnum;
import com.idea.plugin.sql.support.enums.NullTypeEnum;
import com.idea.plugin.sql.support.enums.PrimaryTypeEnum;
import com.idea.plugin.sql.support.enums.ProcedureTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableInfoVO {
    public String author;
    public List<ProcedureTypeEnum> procedureType = new ArrayList<>();
    public String filePath;
    public String fileName;
    public String jdbcUrl;
    public String username;
    public String password;
    public String tableName;
    public String tableComment = "";
    public String insertColumnName;
    public String insertColumnParam;
    public String insertSql;
    public String insertData;
    public List<IndexInfoVO> indexInfos = new ArrayList<>();
    public List<FieldInfoVO> fieldInfos = new ArrayList<>();

    public static TableInfoVO builder() {
        return new TableInfoVO();
    }

    public String getAuthor() {
        return author;
    }

    public List<ProcedureTypeEnum> getProcedureType() {
        return procedureType;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public String getInsertColumnName() {
        return insertColumnName;
    }

    public String getInsertColumnParam() {
        return insertColumnParam;
    }

    public String getInsertData() {
        return insertData;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public List<IndexInfoVO> getIndexInfos() {
        return indexInfos;
    }

    public List<FieldInfoVO> getFieldInfos() {
        return fieldInfos;
    }

    public TableInfoVO author(String author) {
        this.author = author;
        return this;
    }

    public TableInfoVO procedureType(String procedureType) {
        this.procedureType = Arrays.stream(procedureType.split(",")).map(code -> ProcedureTypeEnum.codeToEnum(code.trim())).filter(Objects::nonNull).collect(Collectors.toList());
        return this;
    }

    public TableInfoVO filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public TableInfoVO fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public TableInfoVO jdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    public TableInfoVO username(String username) {
        this.username = username;
        return this;
    }

    public TableInfoVO password(String password) {
        this.password = password;
        return this;
    }

    public TableInfoVO tableName(String tableName) {
        this.tableName = tableName.toUpperCase();
        this.tableComment = tableName.toUpperCase();
        return this;
    }

    public TableInfoVO tableComment(String tableComment) {
        this.tableComment = tableComment;
        return this;
    }

    public TableInfoVO insertColumnName(String insertColumnName, String insertColumnParam) {
        this.insertColumnName = insertColumnName.toUpperCase();
        this.insertColumnParam = insertColumnParam.toUpperCase();
        return this;
    }

    public TableInfoVO insertSql(String insertSql) {
        this.insertSql = insertSql;
        return this;
    }

    public TableInfoVO insertData(String insertData) {
        if (this.insertData == null) {
            this.insertData = insertData;
        } else {
            this.insertData = this.insertData + "\n" + insertData;
        }
        return this;
    }

    public TableInfoVO indexInfos(String indexName, String indexColumnName) {
        if (StringUtils.isEmpty(indexName) || StringUtils.isEmpty(indexColumnName)) {
            return this;
        }
        IndexInfoVO indexInfoVO = IndexInfoVO.builder().indexName(indexName.toUpperCase()).indexColumnName(indexColumnName.toUpperCase());
        this.indexInfos.add(indexInfoVO);
        return this;
    }

    public TableInfoVO fieldInfos(String columnName, String columnType, String comment, String arg) throws SqlException {
        if (StringUtils.isEmpty(columnName) || StringUtils.isEmpty(columnType)) {
            return this;
        }
        FieldInfoVO fieldInfoVO = FieldInfoVO.builder().columnName(columnName.toUpperCase()).columnType(FieldTypeEnum.codeToEnum(columnType.toUpperCase())).columnTypeArgs(FieldTypeEnum.codeGetArgs(columnType.toUpperCase())).comment(comment);
        if (StringUtils.isNotEmpty(arg)) {
            arg = arg.toUpperCase();
            if (NullTypeEnum.codeToEnum(arg) != null) {
                fieldInfoVO.nullType(NullTypeEnum.codeToEnum(arg));
            }
            if (PrimaryTypeEnum.codeToEnum(arg) != null) {
                fieldInfoVO.primary(PrimaryTypeEnum.codeToEnum(arg));
                fieldInfoVO.nullType(NullTypeEnum.NOT_NULL);
            }
        }
        this.fieldInfos.add(fieldInfoVO);
        return this;
    }

}
