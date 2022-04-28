package com.idea.plugin.sql;

import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;

public abstract class BaseProcedureService {

    public abstract void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException;

}
