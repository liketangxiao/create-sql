package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.DataTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import com.idea.plugin.sql.utils.DBProcedureUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MysqlProcedureInsertSqlService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(tableInfoVO.insertSql)) {
            return;
        }
        IProcedureService procedureService = new MysqlProcedureInsertData();
        writeFile(path, String.format(procedureService.getComment(), tableInfoVO.getTableComment()));
        Connection connection = DBProcedureUtils.getConnection(tableInfoVO);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(tableInfoVO.getInsertSql());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> codeList = new ArrayList<>();
            Set<String> columnClassList = new HashSet<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            String idCode = metaData.getColumnLabel(1);
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                codeList.add(metaData.getColumnLabel(i));
                columnClassList.add(metaData.getColumnClassName(i));
            }
            String codes = String.join(", ", codeList);
            while (resultSet.next()) {
                String idValue = "'" + resultSet.getString(1) + "'";
                List<String> rowValues = DBProcedureUtils.getRowValues(DataTypeEnum.MYSQL, resultSet, metaData, null, null, null);
                String values = String.join(", ", rowValues);
                writeFile(path, String.format(procedureService.getProcedure(), tableInfoVO.getTableName(), codes, values, tableInfoVO.getTableName(), idCode, idValue));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
