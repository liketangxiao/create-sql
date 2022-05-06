package com.idea.plugin.orm.support.enums;

import com.google.common.base.CaseFormat;
import com.idea.plugin.sql.support.TableInfoVO;
import org.apache.commons.lang3.StringUtils;

public enum FileTypePathEnum {
    DO("/model/entity", "dto/do.ftl", "%sDO", FileTypeEnum.JAVA),
    VO("/model/vo", "dto/vo.ftl", "%sVO", FileTypeEnum.JAVA),
    DAO("/dao", "orm/dao.ftl", "I%sDAO", FileTypeEnum.JAVA),
    MAPPER("/dao", "orm/mapper.ftl", "I%sDAO", FileTypeEnum.XML),
    MAPPER_MYSQL("/dao/mysql", "orm/mapperMysql.ftl", "I%sDAO_mysql", FileTypeEnum.XML),
    MAPPER_ORACLE("/dao/oracle", "orm/mapperOracle.ftl", "I%sDAO_oracle", FileTypeEnum.XML),
    ISERVICE("/service", "service/iservice.ftl", "I%sService", FileTypeEnum.JAVA),
    SERVICE("/service/impl", "service/service.ftl", "%sService", FileTypeEnum.JAVA),
    CONTROLLER("/controller", "service/controller.ftl", "%sController", FileTypeEnum.JAVA),
    ;
    String javapath;
    String ftlpath;
    String fileName;
    FileTypeEnum fileType;

    FileTypePathEnum(String javapath, String ftlpath, String fileName, FileTypeEnum fileType) {
        this.javapath = javapath;
        this.ftlpath = ftlpath;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public String getJavapath(String path) {
        return path + javapath;
    }

    public String getJavapath(String path, TableInfoVO tableInfoVO) {
        String subpath;
        switch (this) {
            case DO:
                subpath = StringUtils.isEmpty(tableInfoVO.doPath) ? javapath : tableInfoVO.doPath;
                break;
            case VO:
                subpath = StringUtils.isEmpty(tableInfoVO.voPath) ? javapath : tableInfoVO.voPath;
                break;
            case DAO:
            case MAPPER:
                subpath = StringUtils.isEmpty(tableInfoVO.daoPath) ? javapath : tableInfoVO.daoPath;
                break;
            case MAPPER_MYSQL:
                subpath = StringUtils.isEmpty(tableInfoVO.daoMysqlPath) ? javapath : tableInfoVO.daoMysqlPath;
                break;
            case MAPPER_ORACLE:
                subpath = StringUtils.isEmpty(tableInfoVO.daoOraclePath) ? javapath : tableInfoVO.daoOraclePath;
                break;
            case ISERVICE:
                subpath = StringUtils.isEmpty(tableInfoVO.iservicePath) ? javapath : tableInfoVO.iservicePath;
                break;
            case SERVICE:
                subpath = StringUtils.isEmpty(tableInfoVO.servicePath) ? javapath : tableInfoVO.servicePath;
                break;
            case CONTROLLER:
                subpath = StringUtils.isEmpty(tableInfoVO.controllerPath) ? javapath : tableInfoVO.controllerPath;
                break;
            default:
                subpath = javapath;
                break;
        }
        if (!subpath.startsWith("/")) {
            subpath = "/" + subpath;
        }
        return path + subpath;
    }

    public String getFtlpath() {
        return ftlpath;
    }

    public String getFileName(String tableName) {
        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.replaceAll("(^[A-Z]){1}[_]{1}", ""));
        return String.format(fileName, name);
    }

    public FileTypeEnum getFileType() {
        return fileType;
    }

}
