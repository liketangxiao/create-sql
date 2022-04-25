package com.idea.plugin.orm.support.enums;

import com.google.common.base.CaseFormat;

public enum FileTypePathEnum {
    ENTITY("%s/model/entity", "orm/entity.ftl", "%sDO", FileTypeEnum.JAVA),
    VO("%s/model/vo", "orm/vo.ftl", "%sVO", FileTypeEnum.JAVA),
    DAO("%s/dao", "orm/dao.ftl", "I%sDAO", FileTypeEnum.JAVA),
    MAPPER("%s/dao", "orm/mapper.ftl", "I%sDAO", FileTypeEnum.XML),
    ISERVICE("%s/service", "service/iservice.ftl", "I%sService", FileTypeEnum.JAVA),
    SERVICE("%s/service/impl", "service/service.ftl", "%sService", FileTypeEnum.JAVA),
    CONTROLLER("%s/controller", "service/controller.ftl", "%sController", FileTypeEnum.JAVA),
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
        return String.format(javapath, path);
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
