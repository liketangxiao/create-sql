package com.idea.plugin.orm.service;

import com.idea.plugin.orm.support.FileTypeInfo;
import com.idea.plugin.orm.support.GeneratorContext;
import com.idea.plugin.orm.support.TableModule;
import com.idea.plugin.orm.support.TableModuleFactory;
import com.idea.plugin.orm.support.enums.FileTypeEnum;
import com.idea.plugin.orm.support.enums.FileTypePathEnum;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;


public class ProjectGenerator extends GeneratorConfig {

    public void generationFile(GeneratorContext context) {
        try {
            getFileTypeInfo(context);
            TableModuleFactory.createTableModule(context);
            writeFile(context.getFileTypeInfo().getAbsulotePath(), getTemplate(context));
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void getFileTypeInfo(GeneratorContext context) throws SqlException {
        TableInfoVO tableInfoVO = context.getTableInfoVO();
        FileTypePathEnum fileTypePathEnum = context.getFileType();
        FileTypeInfo fileTypeInfo = new FileTypeInfo();
        context.setFileTypeInfo(fileTypeInfo);
        String modulePath = tableInfoVO.modulePath.trim();
        if (modulePath.lastIndexOf("/") == modulePath.length() - 1) {
            modulePath = modulePath.substring(0, modulePath.lastIndexOf("/"));
        }
        String moduleFullName = modulePath.substring(modulePath.lastIndexOf("/") + 1);
        File file = new File(modulePath);
        if (!file.exists() && !file.isDirectory()) {
            throw new SqlException(String.format("模块路径：%s不存在", modulePath));
        }
        FileTypeEnum fileType = fileTypePathEnum.getFileType();
        String subModulePath = moduleFullName.toLowerCase().replaceAll("\\.", "/");
        String moduleName = moduleFullName;
        if (moduleFullName.lastIndexOf(".") > 0) {
            moduleName = moduleFullName.substring(moduleFullName.lastIndexOf(".") + 1);
        }
        String packgPath = fileTypePathEnum.getJavapath(subModulePath);
        String fileName = fileTypePathEnum.getFileName(tableInfoVO.tableName);
        VirtualFile virtualFile = createPackageDir(modulePath + "/" + fileType.getPath() + packgPath);
        fileTypeInfo.setModulePath(subModulePath);
        fileTypeInfo.setModuleName(moduleName);
        fileTypeInfo.setPackagePath(packgPath);
        fileTypeInfo.setFileName(fileName);
        fileTypeInfo.setFileType(fileType.getType());
        fileTypeInfo.setFileTypePath(fileType.getPath());
        fileTypeInfo.setSourceRoot(subModulePath + "/" + fileName);
        fileTypeInfo.setProjectRoot(fileType.getPath() + subModulePath + "/" + fileName + "." + fileType.getType());
        fileTypeInfo.setAbsulotePath(virtualFile.getPath() + "/" + fileName + "." + fileType.getType());
    }

    private <T extends TableModule> String getTemplate(GeneratorContext context) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Template template = super.getTemplate(context.getFileType().getFtlpath());
        template.process(context.getTableModule(), stringWriter);
        return stringWriter.toString();
    }

    private static VirtualFile createPackageDir(String packageName) {
        String path = FileUtil.toSystemIndependentName(packageName);
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }
}
