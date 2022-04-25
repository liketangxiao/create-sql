package com.idea.plugin.orm.support;

import com.idea.plugin.orm.support.enums.FileTypePathEnum;
import com.idea.plugin.sql.support.TableInfoVO;

public class GeneratorContext {
    private TableInfoVO tableInfoVO;
    private FileTypePathEnum fileType;
    private TableModule tableModule;
    private FileTypeInfo fileTypeInfo;

    public GeneratorContext(TableInfoVO tableInfoVO, FileTypePathEnum fileType) {
        this.tableInfoVO = tableInfoVO;
        this.fileType = fileType;
    }

    public TableInfoVO getTableInfoVO() {
        return tableInfoVO;
    }

    public void setTableInfoVO(TableInfoVO tableInfoVO) {
        this.tableInfoVO = tableInfoVO;
    }

    public FileTypePathEnum getFileType() {
        return fileType;
    }

    public void setFileType(FileTypePathEnum fileType) {
        this.fileType = fileType;
    }

    public TableModule getTableModule() {
        return tableModule;
    }

    public void setTableModule(TableModule tableModule) {
        this.tableModule = tableModule;
    }

    public FileTypeInfo getFileTypeInfo() {
        return fileTypeInfo;
    }

    public void setFileTypeInfo(FileTypeInfo fileTypeInfo) {
        this.fileTypeInfo = fileTypeInfo;
    }
}
