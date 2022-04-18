package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OracleProcedureAddDataService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        IProcedureService procedureService = new OracleProcedureAddData();
        writeFile(path, String.format(procedureService.getComment(), tableInfoVO.getTableComment()));
        String[] columnNameArr = tableInfoVO.getInsertColumnName().split(",");
        String columnNameValue = Arrays.stream(Arrays.copyOfRange(columnNameArr, 1, columnNameArr.length))
                .map(columnName -> "V_TABLE_DATA." + columnName.trim()).collect(Collectors.joining(", "));
        String columnParams = "P_PARAM  IN VARCHAR";
        String columnCondition = "PARAM = P_PARAM";
        if (tableInfoVO.getInsertColumnParam() != null) {
            String[] columnParamArr = tableInfoVO.getInsertColumnParam().split(",");
            columnParams = Arrays.stream(columnParamArr)
                    .map(columnParam -> "P_" + columnParam.trim() + " IN VARCHAR").collect(Collectors.joining(", "));
            columnCondition = columnParamArr[0].trim() + " = " + "P_" + columnParamArr[0].trim();
        }
        String procedure = String.format(procedureService.getProcedure(),
                tableInfoVO.getTableName(), columnParams, tableInfoVO.getTableName() + "%ROWTYPE",
                tableInfoVO.getTableName(), columnCondition, tableInfoVO.getTableName(), columnCondition,
                tableInfoVO.getTableName(), tableInfoVO.getInsertColumnName(), columnNameValue, tableInfoVO.getTableName());
        writeFile(path, procedure);
        writeFile(path, String.format(procedureService.getCall(), tableInfoVO.getTableName()));
        writeFile(path, String.format(procedureService.getDrop(), tableInfoVO.getTableName()));
    }
}
