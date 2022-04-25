package com.idea.plugin.utils;

import com.idea.plugin.orm.service.ProjectGenerator;
import com.idea.plugin.orm.support.GeneratorContext;
import com.idea.plugin.orm.support.enums.FileTypePathEnum;
import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.support.ProcedureVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.DataProcedureTypeEnum;
import com.idea.plugin.sql.support.enums.DataTypeEnum;
import com.idea.plugin.sql.support.enums.FileDDLTypeEnum;
import com.idea.plugin.sql.support.enums.ProcedureTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreateFileUtils {

    public static void generatorSqlFile(ProcedureVO procedureVO) throws SqlException, IllegalAccessException, NoSuchFieldException {
        AssertUtils.assertIsTrue(StringUtils.isNotEmpty(procedureVO.filePath), "文件路径filePath不能为空");
        AssertUtils.assertIsTrue(CollectionUtils.isNotEmpty(procedureVO.tableInfoVOS), "生成文件类型procedureType不能为空");
        List<TableInfoVO> tableInfoVOS = procedureVO.tableInfoVOS.stream().filter(tableInfoVO -> tableInfoVO.procedureType.stream().anyMatch(procedureTypeEnum -> procedureTypeEnum.getFileType() != null)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tableInfoVOS)) {
            return;
        }
        List<FileDDLTypeEnum> fileDDLTypeEnums = procedureVO.tableInfoVOS.stream().flatMap(tableInfoVO -> tableInfoVO.procedureType.stream()).map(ProcedureTypeEnum::getFileType).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        FileDDLTypeEnum fileType = FileDDLTypeEnum.getFirstFileType(fileDDLTypeEnums);
        LocalDateTime localDateTime = LocalDateTime.now();
        String mysqlPath = getFilePath(DataTypeEnum.MYSQL, fileType, procedureVO.filePath, procedureVO.fileName, localDateTime);
        String oralcePath = getFilePath(DataTypeEnum.ORACLE, fileType, procedureVO.filePath, procedureVO.fileName, localDateTime);
        TableInfoVO initialTableInfoVO = TableInfoVO.builder();
        initialTableInfoVO.procedureType.add(ProcedureTypeEnum.INITIAL);
        procedureVO.tableInfoVOS.add(initialTableInfoVO);
        Class<TableInfoVO> tableInfoVOClass = TableInfoVO.class;
        for (TableInfoVO tableInfoVO : tableInfoVOS) {
            for (ProcedureTypeEnum procedureTypeEnum : tableInfoVO.procedureType) {
                if (procedureTypeEnum != null && !ProcedureTypeEnum.ORM_GENERATE.equals(procedureTypeEnum)) {
                    for (String fieldName : procedureTypeEnum.getMustFieldList()) {
                        Field field = tableInfoVOClass.getField(fieldName);
                        Object value = field.get(tableInfoVO);
                        if (value == null || StringUtils.isEmpty(value.toString())) {
                            throw new SqlException(String.format("生成脚本类型：%s 属性值：%s不能为空", procedureTypeEnum.name(), fieldName));
                        }
                    }
                    BaseProcedureService mysqlProcedureService = DataProcedureTypeEnum.getProcedureService(procedureTypeEnum, DataTypeEnum.MYSQL);
                    BaseProcedureService oralceProcedureService = DataProcedureTypeEnum.getProcedureService(procedureTypeEnum, DataTypeEnum.ORACLE);
                    if (mysqlProcedureService != null) {
                        mysqlProcedureService.addProcedure(mysqlPath, tableInfoVO);
                    }
                    if (oralceProcedureService != null) {
                        oralceProcedureService.addProcedure(oralcePath, tableInfoVO);
                    }
                }
            }
        }
    }


    public static String getFilePath(DataTypeEnum dataType, FileDDLTypeEnum fileType, String filePath, String fileName, LocalDateTime localDateTime) throws SqlException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");
        File file = new File(filePath);
        filePath = filePath + "/" + dataType.getCode();
        if (!file.exists() && !file.isDirectory()) {
            throw new SqlException(String.format("文件：%s不存在", filePath));
        } else {
            file = new File(filePath);
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
        }
        return filePath + "/V" + dateFormat.format(localDateTime) + "__" + fileType.getCode() + "." + fileName + "_" + dataType.getCode() + ".sql";
    }


    public static void generatorJavaFile(ProcedureVO procedureVO) throws Exception {
        AssertUtils.assertIsTrue(StringUtils.isNotEmpty(procedureVO.modulePath), "文件路径modulePath不能为空");
        AssertUtils.assertIsTrue(CollectionUtils.isNotEmpty(procedureVO.tableInfoVOS), "生成文件类型procedureType不能为空");
        List<TableInfoVO> tableInfoVOS = procedureVO.tableInfoVOS.stream().filter(tableInfoVO -> tableInfoVO.procedureType.contains(ProcedureTypeEnum.ORM_GENERATE)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tableInfoVOS)) {
            return;
        }
        ProjectGenerator projectGenerator = new ProjectGenerator();
        Class<TableInfoVO> tableInfoVOClass = TableInfoVO.class;
        for (TableInfoVO tableInfoVO : tableInfoVOS) {
            for (String fieldName : ProcedureTypeEnum.ORM_GENERATE.getMustFieldList()) {
                Field field = tableInfoVOClass.getField(fieldName);
                Object value = field.get(tableInfoVO);
                if (value == null || StringUtils.isEmpty(value.toString())) {
                    throw new SqlException(String.format("生成脚本类型：%s 属性值：%s不能为空", ProcedureTypeEnum.ORM_GENERATE.name(), fieldName));
                }
            }
            DBUtils.getTableInfoVOFromDB(tableInfoVO);
            for (FileTypePathEnum fileTypePathEnum : FileTypePathEnum.values()) {
                GeneratorContext context = new GeneratorContext(tableInfoVO, fileTypePathEnum);
                projectGenerator.generationFile(context);
            }
        }
    }

}
