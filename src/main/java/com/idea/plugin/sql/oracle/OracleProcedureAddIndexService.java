package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.IndexInfoVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class OracleProcedureAddIndexService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || CollectionUtils.isEmpty(tableInfoVO.getIndexInfos())) {
            return;
        }
        IProcedureService procedureService = new OracleProcedureAddIndex();
        writeFile(path, String.format(procedureService.getComment(), tableInfoVO.getTableComment()));
        writeFile(path, String.format(procedureService.getProcedure(), tableInfoVO.getTableName(), tableInfoVO.getTableName()));
        for (IndexInfoVO indexInfoVO : tableInfoVO.getIndexInfos()) {
            String alterAddIndexCall = String.format(procedureService.getCall(), tableInfoVO.getTableName(), indexInfoVO.getIndexName(), indexInfoVO.getIndexColumnName());
            writeFile(path, alterAddIndexCall);
        }
        writeFile(path, String.format(procedureService.getDrop(), tableInfoVO.getTableName()));
    }
}
