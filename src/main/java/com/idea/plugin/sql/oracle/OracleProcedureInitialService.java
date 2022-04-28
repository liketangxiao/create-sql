package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;
import com.idea.plugin.utils.FileUtils;

public class OracleProcedureInitialService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        FileUtils.writeFile(path, "-- " + (tableInfoVO.author == null ? "" : tableInfoVO.author) + " " + tableInfoVO.fileName + "\n");
    }
}
