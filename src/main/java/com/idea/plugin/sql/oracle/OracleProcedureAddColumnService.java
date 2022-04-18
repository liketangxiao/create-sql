package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.FieldInfoVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class OracleProcedureAddColumnService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || CollectionUtils.isEmpty(tableInfoVO.fieldInfos)) {
            return;
        }
        IProcedureService procedureService = new OracleProcedureAddColumn();
        writeFile(path, String.format(procedureService.getComment(), tableInfoVO.comment));
        writeFile(path, String.format(procedureService.getProcedure(), tableInfoVO.tableName, tableInfoVO.tableName));
        for (FieldInfoVO fieldVO : tableInfoVO.fieldInfos) {
            String alterAddColumnCall = String.format(procedureService.getCall(),
                    tableInfoVO.tableName, tableInfoVO.tableName, fieldVO.columnName, fieldVO.columnType.getOtype(fieldVO.columnTypeArgs), fieldVO.nullType.getCode(), fieldVO.comment);
            writeFile(path, alterAddColumnCall);
        }
        writeFile(path, String.format(procedureService.getDrop(), tableInfoVO.tableName));
    }
}
