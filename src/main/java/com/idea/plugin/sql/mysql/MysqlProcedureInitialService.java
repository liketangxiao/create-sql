package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;

public class MysqlProcedureInitialService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        writeFile(path, "-- " + tableInfoVO.getAuthor() + " " + tableInfoVO.getFileName() + "\n");
    }
}
