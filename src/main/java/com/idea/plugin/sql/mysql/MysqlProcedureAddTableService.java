package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.PrimaryTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.stream.Collectors;

public class MysqlProcedureAddTableService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || CollectionUtils.isEmpty(tableInfoVO.getFieldInfos())) {
            return;
        }
        IProcedureService procedureService = new MysqlProcedureAddTable();
        writeFile(path, String.format(procedureService.getComment(), tableInfoVO.getTableComment()));
        String procedure = procedureService.getProcedure();
        Integer length = tableInfoVO.getFieldInfos().stream().map(fieldInfo -> fieldInfo.getColumnName().length()).max(Comparator.comparing(Integer::intValue)).get();
        String call = tableInfoVO.getFieldInfos().stream().map(fieldVO -> {
                    String format = String.format(procedureService.getCall(), getColumnName(fieldVO.getColumnName(), length), fieldVO.getColumnType().getMtype(fieldVO.columnTypeArgs), fieldVO.getNullType().getCode(), fieldVO.getComment());
                    if (PrimaryTypeEnum.PRIMARY.equals(fieldVO.getPrimary())) {
                        format = format + fieldVO.getPrimary().getCode();
                    }
                    return format;
                })
                .collect(Collectors.joining(",\n"));
        procedure = String.format(procedure, tableInfoVO.getTableName(), call, tableInfoVO.getTableComment());
        writeFile(path, procedure);
    }

    private String getColumnName(String columnName, Integer length) {
        for (int i = columnName.length(); i < length; i++) {
            columnName += " ";
        }
        return columnName;
    }
}
