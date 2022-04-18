package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.IndexInfoVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class MysqlProcedureAddIndexService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || CollectionUtils.isEmpty(tableInfoVO.indexInfos)) {
            return;
        }
        IProcedureService procedureService = new MysqlProcedureAddIndex();
        String comment = StringUtils.isEmpty(tableInfoVO.comment) ? tableInfoVO.tableComment + "新增索引" : tableInfoVO.comment;
        writeFile(path, String.format(procedureService.getComment(), comment));
        writeFile(path, String.format(procedureService.getProcedure(), tableInfoVO.tableName, tableInfoVO.tableName));
        for (IndexInfoVO indexInfoVO : tableInfoVO.indexInfos) {
            String alterAddIndexCall = String.format(procedureService.getCall(), tableInfoVO.tableName, tableInfoVO.tableName, indexInfoVO.indexName, indexInfoVO.indexColumnName);
            writeFile(path, alterAddIndexCall);
        }
        writeFile(path, String.format(procedureService.getDrop(), tableInfoVO.tableName));
    }
}
