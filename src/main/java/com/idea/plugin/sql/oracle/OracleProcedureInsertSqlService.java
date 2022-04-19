package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.DataTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import com.idea.plugin.utils.DBProcedureUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class OracleProcedureInsertSqlService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(tableInfoVO.insertSql)) {
            return;
        }
        IProcedureService procedureService = new OracleProcedureInsertData();
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
                codeList.add(metaData.getColumnLabel(i));
            }
            String codes = String.join(", ", codeList);
            while (resultSet.next()) {
                String idValue = DBProcedureUtils.getIdValue(resultSet.getString(1));
                List<String> declareColumns = new ArrayList<>();
                List<String> dbmsLobCreates = new ArrayList<>();
                List<String> dbmsLobApends = new ArrayList<>();
                List<String> rowValues = DBProcedureUtils.getRowValues(DataTypeEnum.ORACLE, resultSet, metaData, declareColumns, dbmsLobCreates, dbmsLobApends);
                String values = String.join(", ", rowValues);
                if (CollectionUtils.isNotEmpty(declareColumns)) {
                    String declareColumn = String.join("", declareColumns);
                    String dbmsLobCreate = String.join("", dbmsLobCreates);
                    String dbmsLobApend = String.join("", dbmsLobApends);
                    writeFile(path, String.format(procedureService.getCall(), declareColumn, dbmsLobCreate, dbmsLobApend, tableInfoVO.tableName, codes, values, tableInfoVO.tableName, idCode, idValue));
                } else {
                    writeFile(path, String.format(procedureService.getProcedure(), tableInfoVO.tableName, codes, values, tableInfoVO.tableName, idCode, idValue));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
