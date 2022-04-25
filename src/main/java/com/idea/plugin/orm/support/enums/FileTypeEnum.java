package com.idea.plugin.orm.support.enums;

public enum FileTypeEnum {
    JAVA("java", "src/main/java/"),
    XML("xml", "src/main/resources/"),
    ;
    String type;
    String path;

    FileTypeEnum(String type, String path) {
        this.type = type;
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }
}
