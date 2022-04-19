package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.DataTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import com.idea.plugin.utils.DBProcedureUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class MysqlProcedureInsertSqlService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(tableInfoVO.insertSql)) {
            return;
        }
        IProcedureService procedureService = new MysqlProcedureInsertData();
        String comment = StringUtils.isEmpty(tableInfoVO.comment) ? tableInfoVO.tableComment + "新增数据" : tableInfoVO.comment;
        writeFile(path, String.format(procedureService.getComment(), comment));
        Connection connection = DBProcedureUtils.getConnection(tableInfoVO);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(tableInfoVO.insertSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> codeList = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            String idCode = metaData.getColumnLabel(1);
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                codeList.add(metaData.getColumnLabel(i).toUpperCase());
            }
            String codes = String.join(", ", codeList);
            while (resultSet.next()) {
                List<String> rowValues = new ArrayList<>();
                String idValue = DBProcedureUtils.getIdValue(resultSet.getString(1));
                rowValues.add(idValue);
                DBProcedureUtils.getRowValues(rowValues, DataTypeEnum.MYSQL, resultSet, metaData, null, null, null);
                String values = String.join(", ", rowValues);
                writeFile(path, String.format(procedureService.getProcedure(), tableInfoVO.tableName, codes, values, tableInfoVO.tableName, idCode, idValue));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
