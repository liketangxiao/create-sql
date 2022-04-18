package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.PrimaryTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.stream.Collectors;

public class OracleProcedureAddTableService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || CollectionUtils.isEmpty(tableInfoVO.getFieldInfos())) {
            return;
        }
        OracleProcedureAddTable procedureService = new OracleProcedureAddTable();
        String procedure = procedureService.getProcedure();
        Integer length = tableInfoVO.getFieldInfos().stream().map(fieldInfo -> fieldInfo.getColumnName().length()).max(Comparator.comparing(Integer::intValue)).get();
        String call = tableInfoVO.getFieldInfos().stream().map(fieldVO -> {
            String format = String.format(procedureService.getCall(), getColumnName(fieldVO.getColumnName(), length), fieldVO.getColumnType().getOtype(fieldVO.columnTypeArgs), fieldVO.getNullType().getCode());
            if (PrimaryTypeEnum.PRIMARY.equals(fieldVO.getPrimary())) {
                format = format + String.format("\n                    CONSTRAINT %s_PK PRIMARY KEY", tableInfoVO.getTableName());
            }
            return format;
        }).collect(Collectors.joining(",\n"));
        String callComment = tableInfoVO.getFieldInfos().stream().map(fieldVO -> String.format(procedureService.getCallComment(), fieldVO.getColumnName(), fieldVO.getComment())).collect(Collectors.joining("\n"));
        procedure = String.format(procedure, tableInfoVO.getTableName(), tableInfoVO.getTableName(), call, tableInfoVO.getTableName(), tableInfoVO.getTableComment(), callComment);
        writeFile(path, procedure);
    }

    private String getColumnName(String columnName, Integer length) {
        for (int i = columnName.length(); i < length; i++) {
            columnName += " ";
        }
        return columnName;
    }
}
