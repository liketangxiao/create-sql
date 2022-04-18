package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;

public class OracleProcedureInitialService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        writeFile(path, "-- " + tableInfoVO.getAuthor() + " " + tableInfoVO.getFileName() + "\n");
    }
}
