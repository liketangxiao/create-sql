package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.PrimaryTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import com.idea.plugin.utils.FileUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.stream.Collectors;

public class OracleProcedureAddTableService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || CollectionUtils.isEmpty(tableInfoVO.fieldInfos)) {
            return;
        }
        OracleProcedureAddTable procedureService = new OracleProcedureAddTable();
        String comment = StringUtils.isEmpty(tableInfoVO.comment) ? tableInfoVO.tableComment + "新增表" : tableInfoVO.comment;
        FileUtils.writeFile(path, String.format(procedureService.getComment(), comment));
        String procedure = procedureService.getProcedure();
        Integer length = tableInfoVO.fieldInfos.stream().map(fieldInfo -> fieldInfo.columnName.length()).max(Comparator.comparing(Integer::intValue)).get();
        String call = tableInfoVO.fieldInfos.stream().map(fieldVO -> {
            String format = String.format(procedureService.getCall(), getColumnName(fieldVO.columnName, length), fieldVO.columnType.getOtype(fieldVO.columnTypeArgs), fieldVO.nullType.getCode());
            if (PrimaryTypeEnum.PRIMARY.equals(fieldVO.primary)) {
                format = format + String.format("\n                    CONSTRAINT %s_PK PRIMARY KEY", tableInfoVO.tableName);
            }
            return format;
        }).collect(Collectors.joining(",\n"));
        String callComment = tableInfoVO.fieldInfos.stream().map(fieldVO -> String.format(procedureService.getCallComment(), fieldVO.columnName, fieldVO.comment)).collect(Collectors.joining("\n"));
        procedure = String.format(procedure, tableInfoVO.tableName, tableInfoVO.tableName, call, tableInfoVO.tableName, tableInfoVO.tableComment, callComment);
        FileUtils.writeFile(path, procedure);
    }

    private String getColumnName(String columnName, Integer length) {
        for (int i = columnName.length(); i < length; i++) {
            columnName += " ";
        }
        return columnName;
    }
}
