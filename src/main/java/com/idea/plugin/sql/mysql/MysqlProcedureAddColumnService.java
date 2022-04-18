package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.FieldInfoVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class MysqlProcedureAddColumnService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || CollectionUtils.isEmpty(tableInfoVO.getFieldInfos())) {
            return;
        }
        IProcedureService procedureService = new MysqlProcedureAddColumn();
        writeFile(path, String.format(procedureService.getComment(), tableInfoVO.getTableComment()));
        writeFile(path, String.format(procedureService.getProcedure(), tableInfoVO.getTableName(), tableInfoVO.getTableName()));
        for (FieldInfoVO fieldVO : tableInfoVO.getFieldInfos()) {
            String alterAddColumnCall = String.format(procedureService.getCall(),
                    tableInfoVO.getTableName(), tableInfoVO.getTableName(), fieldVO.getColumnName(), fieldVO.getColumnType().getMtype(fieldVO.columnTypeArgs), fieldVO.getNullType().getCode(), fieldVO.getComment());
            writeFile(path, alterAddColumnCall);
        }
        writeFile(path, String.format(procedureService.getDrop(), tableInfoVO.getTableName()));
    }
}
