package com.idea.plugin.build;

import com.idea.plugin.orm.support.enums.FileTypeEnum;
import com.idea.plugin.ui.CopyBuildUI;
import com.idea.plugin.utils.FileUtils;
import com.idea.plugin.utils.ZipUtils;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BuildService {

    public static void copyBuildFile(Project project, VirtualFile[] virtualFiles) {
        if (virtualFiles.length <= 0) {
            Messages.showErrorDialog("请选择文件！", "错误");
            return;
        }
        BuildSettings config = BuildSettings.getInstance(ProjectManager.getInstance().getDefaultProject());
        try {
            String fileNameStr = "";
            String absolutePath = config.getBuildConfigVO().filePathCache;
            String folderName = config.getBuildConfigVO().folderName;
            File file = new File(absolutePath + "/" + folderName);
            if (StringUtils.isEmpty(folderName) || !file.exists()) {
                CopyBuildUI copyBuildUI = new CopyBuildUI(project);
                folderName = "默认文件名称";
                if (copyBuildUI.showAndGet()) {
                    String name = copyBuildUI.getFolderName();
                    if (StringUtils.isNotEmpty(name)) {
                        folderName = name;
                        config.getBuildConfigVO().folderName = name;
                    }
                }
            }
            if (StringUtils.isEmpty(absolutePath)) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int res = chooser.showSaveDialog(new JLabel());
                if (JFileChooser.APPROVE_OPTION != res) {
                    return;
                }
                absolutePath = chooser.getSelectedFile().getAbsolutePath();
                config.getBuildConfigVO().filePathCache = absolutePath;
            }
            absolutePath = absolutePath + "/" + folderName;
            for (VirtualFile virtualFile : virtualFiles) {
                if (virtualFile.isDirectory()) {
                    continue;
                }
                String filePath = virtualFile.getPath();
                String fileType = virtualFile.getFileType().getName();
                String fileName = virtualFile.getName();
                FileTypeEnum fileTypeEnum = FileTypeEnum.codeToEnum(fileType);
                String filePathStr;
                if (fileTypeEnum != null) {
                    String targetPath = filePath.replaceAll(fileTypeEnum.getPath(), "target/classes/");
                    filePathStr = targetPath.substring(targetPath.lastIndexOf("target/classes/") + 15);
                    if (FileTypeEnum.JAVA.equals(fileTypeEnum)) {
                        targetPath = targetPath.replaceAll("\\.java", "\\.class");
                        fileName = fileName.replaceAll("\\.java", "\\.class");
                        int subClassNum = 1;
                        boolean hasSubClass = true;
                        while (hasSubClass) {
                            int i = targetPath.lastIndexOf(".");
                            String subTargetPath = targetPath.substring(0, i) + "$" + subClassNum + targetPath.substring(i);
                            int j = fileName.lastIndexOf(".");
                            String subFileName = fileName.substring(0, j) + "$" + subClassNum + fileName.substring(j);
                            File newfile = new File(subTargetPath);
                            if (newfile.exists()) {
                                ++subClassNum;
                                String path = absolutePath + "/" + subFileName;
                                FileUtils.copyFile(targetPath, path);
                            } else {
                                hasSubClass = false;
                            }
                        }
                    }
                    String path = absolutePath + "/" + fileName;
                    FileUtils.copyFile(targetPath, path);
                } else {
                    Module[] modules = ModuleManager.getInstance(project).getModules();
                    List<String> modulePaths = Arrays.stream(modules).map(ModuleUtil::getModuleDirPath).collect(Collectors.toList());
                    String modulePath = modulePaths.stream().filter(filePath::contains).findAny().orElse("");
                    filePathStr = filePath.replaceAll(modulePath, "");
                    FileUtils.copyFile(filePath, absolutePath + "/" + fileName);
                }
                fileNameStr = fileNameStr + fileName + ";";
                String pathtxt = absolutePath + "/增量地址.txt";
                if (!FileUtils.readFile(pathtxt).contains(filePathStr)) {
                    FileUtils.writeFile(pathtxt, filePathStr + "\n");
                }
                ZipUtils.fileToZip(absolutePath);
            }
            Messages.showMessageDialog("增量文件导入成功: " + fileNameStr, "正确", Messages.getInformationIcon());
        } catch (Exception ex) {
            Messages.showErrorDialog("增量文件导入失败: " + ex.getLocalizedMessage(), "错误");
        }
    }
}
