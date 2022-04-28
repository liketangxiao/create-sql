package com.idea.plugin.build;

import com.idea.plugin.orm.support.enums.FileTypeEnum;
import com.idea.plugin.utils.FileUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

public class BuildService {

    public static void copyBuildFile(Project project, VirtualFile virtualFile) {
        BuildSettings config = BuildSettings.getInstance(ProjectManager.getInstance().getDefaultProject());
        String filePath = virtualFile.getPath();
        String fileType = virtualFile.getFileType().getName();
        String fileName = virtualFile.getName();
        FileTypeEnum fileTypeEnum = FileTypeEnum.codeToEnum(fileType);
        if (fileTypeEnum != null) {
            try {
                String targetPath = filePath.replaceAll(fileTypeEnum.getPath(), "target/classes/");
                String filePathStr = targetPath.substring(targetPath.lastIndexOf("target/classes/") + 15);
                if (FileTypeEnum.JAVA.equals(fileTypeEnum)) {
                    targetPath = targetPath.replaceAll(".java", ".class");
                    fileName = fileName.replaceAll(".java", ".class");
                }
                String absolutePath = config.getBuildConfigVO().filePathCache;
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
                String path = absolutePath + "/" + fileName;
                String pathtxt = absolutePath + "/增量地址.txt";
                FileUtils.copyFile(targetPath, path);
                FileUtils.writeFileAdd(pathtxt, filePathStr + "\n");
                Messages.showMessageDialog("增量文件导入成功, 路径: " + path, "正确", Messages.getInformationIcon());
            } catch (Exception ex) {
                Messages.showErrorDialog("增量文件导入失败: " + ex.getLocalizedMessage(), "错误");
            }
        }
    }
}
