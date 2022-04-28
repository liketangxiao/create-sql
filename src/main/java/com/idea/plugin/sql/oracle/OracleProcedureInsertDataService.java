package com.idea.plugin.sql.oracle;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;
import com.idea.plugin.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class OracleProcedureInsertDataService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(tableInfoVO.insertData)) {
            return;
        }
        IProcedureService procedureService = new OracleProcedureInsertData();
        String comment = StringUtils.isEmpty(tableInfoVO.comment) ? tableInfoVO.tableComment + "新增数据" : tableInfoVO.comment;
        FileUtils.writeFile(path, String.format(procedureService.getComment(), comment));
        Pattern pattern = Pattern.compile("^'\\d{4}-\\d{2}-\\d{2}");
        for (String insertSql : tableInfoVO.insertData.split("\n")) {
            int s1 = insertSql.indexOf("(");
            int s2 = insertSql.indexOf(")");
            int s3 = insertSql.lastIndexOf("(");
            int s4 = insertSql.lastIndexOf(")");
            String codes = insertSql.substring(s1 + 1, s2).toUpperCase();
            String values = insertSql.substring(s3 + 1, s4);
            String[] codeArr = codes.split(",");
            String idCode = codeArr[0];
            String[] valuesArr = values.split(",");
            String idValue = valuesArr[0];
            List<String> valueList = new ArrayList<>();
            for (int i = 0; i < valuesArr.length; i++) {
                String value = valuesArr[i];
                value = value.trim();
                if (pattern.matcher(value).find()) {
                    if ("CREATE_DATE".equals(codeArr[i]) || "UPDATE_DATE".equals(codeArr[i])) {
                        valueList.add("SYSDATE");
                    } else {
                        valueList.add("TIMESTAMP " + value);
                    }
                } else {
                    valueList.add(value);
                }
            }
            values = String.join(", ", valueList);
            FileUtils.writeFile(path, String.format(procedureService.getProcedure(), tableInfoVO.tableName, codes, values, tableInfoVO.tableName, idCode, idValue));
        }
    }
}
