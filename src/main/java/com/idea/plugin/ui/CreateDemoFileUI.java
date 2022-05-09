package com.idea.plugin.ui;

import com.google.common.base.CaseFormat;
import com.idea.plugin.demo.DemoFileStrUtils;
import com.idea.plugin.demo.DemoSettings;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.DataTypeEnum;
import com.idea.plugin.utils.DBUtils;
import com.idea.plugin.utils.FileUtils;
import com.intellij.ide.util.ChooseElementsDialog;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateDemoFileUI {
    private JPanel panel;
    private JPanel sqlPanel;
    private JPanel javaPanel;
    private JPanel commonPanel;
    private JTextField author;
    private JTextField jdbcUrl;
    private JTextField username;
    private JTextField password;
    private JTextField fileName;
    private JTextField filePath;
    private JButton exportSqlButton;
    private JButton exportJavaButton;
    private JTextField modulePath;
    private JCheckBox ADD_TABLE;
    private JCheckBox ADD_INDEX;
    private JCheckBox ADD_COLUMN;
    private JCheckBox INSERT_DATA;
    private JCheckBox INSERT_SQL;
    private JCheckBox ADD_DATA;
    private JCheckBox DO;
    private JCheckBox DAO;
    private JCheckBox SERVICE;
    private JCheckBox CONTROLLER;
    private JButton filepathSelect;
    private JTextField tableName;

    private Project project;
    public DemoSettings config;
    private String selectFilePath;

    public CreateDemoFileUI() {
        config = DemoSettings.getInstance(ProjectManager.getInstance().getDefaultProject());
        this.author.setText(config.getDemoConfigVO().author);
        this.jdbcUrl.setText(config.getDemoConfigVO().jdbcUrl);
        this.username.setText(config.getDemoConfigVO().username);
        this.password.setText(config.getDemoConfigVO().password);
        this.filePath.setText(config.getDemoConfigVO().filePath);
        this.fileName.setText(config.getDemoConfigVO().fileName);
        this.modulePath.setText(config.getDemoConfigVO().modulePath);
        this.tableName.setText(config.getDemoConfigVO().tableName);
        Stream.of(ADD_TABLE, ADD_INDEX, ADD_COLUMN, INSERT_DATA, INSERT_SQL, ADD_DATA, DO, DAO, SERVICE, CONTROLLER).forEach(jCheckBox -> {
            if (config.getDemoConfigVO().getProcedureTypeList().contains(jCheckBox.getText())) {
                jCheckBox.setSelected(true);
            }
        });
        exportSqlButton.addActionListener(e -> {
            try {
                getDemoConfigVOFromDB();
                exportDemoFile(config.getDemoConfigVO().username + "_sql.txt", DemoFileStrUtils.sqlFileStr(config.getDemoConfigVO()));
            } catch (Exception ex) {
                Messages.showMessageDialog("sql配置文件创建失败: " + ex.getLocalizedMessage(), "错误", Messages.getErrorIcon());
                return;
            }
            Messages.showMessageDialog("sql配置文件创建成功", "正确", Messages.getInformationIcon());
        });
        exportJavaButton.addActionListener(e -> {
            try {
                getDemoConfigVOFromDB();
                exportDemoFile(config.getDemoConfigVO().username + "_java.txt", DemoFileStrUtils.javaFileStr(config.getDemoConfigVO()));
            } catch (Exception ex) {
                Messages.showMessageDialog("java配置文件创建失败: " + ex.getLocalizedMessage(), "错误", Messages.getErrorIcon());
                return;
            }
            Messages.showMessageDialog("java配置文件创建成功", "正确", Messages.getInformationIcon());
        });
        modulePath.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Module[] modules = ModuleManager.getInstance(project).getModules();
                List<String> modulePaths = Arrays.stream(modules).map(ModuleUtil::getModuleDirPath).collect(Collectors.toList());
                ChooseStringDialog dialog = new ChooseStringDialog(project, modulePaths, "Choose Module", "Choose Single Module");
                dialog.setSize(400, 400);
                dialog.show();
                List<String> chosenElements = dialog.getChosenElements();
                if (chosenElements.size() > 0) {
                    modulePath.setText(chosenElements.get(0));
                    config.getDemoConfigVO().setModulePath(modulePath.getText());
                }
            }
        });
        filepathSelect.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setCurrentDirectory(new File(project.getBasePath()));
            int res = chooser.showSaveDialog(new JLabel());
            if (JFileChooser.APPROVE_OPTION != res) {
                return;
            }
            File file = chooser.getSelectedFile();
            if (file == null) {
                return;
            }
            this.filePath.setText(file.getAbsolutePath());
            config.getDemoConfigVO().setFilePath(this.filePath.getText());
        });
        tableName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Connection connection = null;
                try {
                    List<String> tableList;
                    String schema = username.getText().toUpperCase();
                    connection = DBUtils.getConnection(jdbcUrl.getText(), username.getText(), password.getText());
                    if (jdbcUrl.getText().contains(DataTypeEnum.MYSQL.getCode())) {
                        schema = connection.getCatalog();
                    }
                    if (config.getDemoConfigVO().tabNameCacheMap.containsKey(schema)) {
                        tableList = config.getDemoConfigVO().tabNameCacheMap.get(schema);
                    } else {
                        tableList = DBUtils.getAllTableName(connection, schema);
                        config.getDemoConfigVO().tabNameCacheMap.put(schema, tableList);
                    }
                    String exitsTableNames = tableName.getText().trim();
                    List<String> exitsTableNameList = new ArrayList<>();
                    String searchName = "";
                    if (exitsTableNames.contains(";")) {
                        String[] exitsTableNameArr = exitsTableNames.split(";");
                        if (exitsTableNames.lastIndexOf(";") < exitsTableNames.length() - 1) {
                            searchName = exitsTableNameArr[exitsTableNameArr.length - 1];
                        }
                        String finalSearchName = searchName;
                        exitsTableNameList = Arrays.stream(exitsTableNameArr).filter(tableName -> StringUtils.isNotEmpty(tableName) && !tableName.equals(finalSearchName)).collect(Collectors.toList());
                    } else {
                        searchName = exitsTableNames;
                    }
                    if (StringUtils.isNotEmpty(searchName) || CollectionUtils.isNotEmpty(exitsTableNameList)) {
                        String searchNameIgnore = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, searchName).toLowerCase();
                        List<String> finalExitsTableNameList = exitsTableNameList;
                        tableList = tableList.stream().filter(tableName -> {
                            String tableNameIgnore = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName).toLowerCase();
                            return tableNameIgnore.contains(searchNameIgnore) && !finalExitsTableNameList.contains(tableName);
                        }).collect(Collectors.toList());
                    }
                    ChooseStringDialog dialog = new ChooseStringDialog(project, tableList, "Choose Module", "Choose Single Module");
                    dialog.setMutilSelectionMode();
                    dialog.setSize(400, 400);
                    dialog.show();
                    StringBuilder tableNameStr = new StringBuilder();
                    for (String tableName : exitsTableNameList) {
                        tableNameStr.append(tableName).append(";");
                    }
                    for (String tableName : dialog.getChosenElements()) {
                        tableNameStr.append(tableName).append(";");
                    }
                    tableName.setText(tableNameStr.toString());
                    config.getDemoConfigVO().setTableName(tableName.getText());
                } catch (Exception ex) {
                    Messages.showMessageDialog("选择表失败: " + ex.getLocalizedMessage(), "错误", Messages.getErrorIcon());
                } finally {
                    DBUtils.close(connection);
                }
            }
        });
        tableName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
    }

    private void getDemoConfigVOFromDB() {
        fillData(project, selectFilePath);
        List<String> tableNameList = config.getDemoConfigVO().getTableNameList();
        List<TableInfoVO> tableInfoVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(tableNameList)) {
            TableInfoVO tableInfoVO = DBUtils.initTableInfoVO(config.getDemoConfigVO());
            tableInfoVOS.add(tableInfoVO);
        } else {
            for (String tableName : tableNameList) {
                if (config.getDemoConfigVO().tableInfoCacheMap.containsKey(tableName)) {
                    TableInfoVO tableInfoVO = config.getDemoConfigVO().tableInfoCacheMap.get(tableName);
                    DBUtils.getTableDataInfo(tableInfoVO);
                    DBUtils.addTableInfoAttri(tableInfoVO);
                    tableInfoVOS.add(tableInfoVO);
                } else {
                    TableInfoVO tableInfoVO = TableInfoVO.builder()
                            .jdbcUrl(config.getDemoConfigVO().jdbcUrl)
                            .username(config.getDemoConfigVO().username)
                            .password(config.getDemoConfigVO().password)
                            .tableInfo(tableName, "");
                    DBUtils.getTableInfo(tableInfoVO);
                    DBUtils.getTableDataInfo(tableInfoVO);
                    DBUtils.addTableInfoAttri(tableInfoVO);
                    config.getDemoConfigVO().tableInfoCacheMap.put(tableName, tableInfoVO);
                    tableInfoVOS.add(tableInfoVO);
                }
            }
        }
        config.getDemoConfigVO().setTableInfoVOS(tableInfoVOS);
    }

    public void exportDemoFile(String demoFileName, String demoFileString) throws Exception {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new File(selectFilePath));
        int res = chooser.showSaveDialog(new JLabel());
        if (JFileChooser.APPROVE_OPTION != res) {
            return;
        }
        File file = chooser.getSelectedFile();
        if (file == null) {
            return;
        }
        FileUtils.writeFileDelete(file.getAbsolutePath() + "/" + demoFileName, demoFileString);
    }

    public JComponent getMianPanel() {
        return panel;
    }

    public void fillData(Project project, String selectFilePath) {
        this.project = project;
        this.selectFilePath = selectFilePath;
        config.getDemoConfigVO().setAuthor(this.author.getText());
        config.getDemoConfigVO().setJdbcUrl(this.jdbcUrl.getText());
        config.getDemoConfigVO().setUsername(this.username.getText());
        config.getDemoConfigVO().setPassword(this.password.getText());
        config.getDemoConfigVO().setFilePath(this.filePath.getText());
        config.getDemoConfigVO().setFileName(this.fileName.getText());
        config.getDemoConfigVO().setModulePath(this.modulePath.getText());
        config.getDemoConfigVO().setTableName(this.tableName.getText());
        config.getDemoConfigVO().setProcedureTypeList(new ArrayList<>());
        Stream.of(ADD_TABLE, ADD_INDEX, ADD_COLUMN, INSERT_DATA, INSERT_SQL, ADD_DATA, DO, DAO, SERVICE, CONTROLLER).forEach(jCheckBox -> {
            if (jCheckBox.isSelected()) {
                config.setProcedureType(jCheckBox.getText());
            }
        });
    }


    private static class ChooseStringDialog extends ChooseElementsDialog<String> {
        public ChooseStringDialog(Project project, List<String> names, String title, String description) {
            super(project, names, title, description, true);
        }

        public void setMutilSelectionMode() {
            JTable jTable = (JTable) myChooser.getComponent();
            jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }

        @Override
        protected String getItemText(String item) {
            return item;
        }

        @Override
        protected Icon getItemIcon(String item) {
            return null;
        }
    }
}
