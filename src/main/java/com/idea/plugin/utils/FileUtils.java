package com.idea.plugin.utils;

import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static void writeFile(String path, String fileStr) throws SqlException {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(fileStr)) {
            return;
        }
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true), StandardCharsets.UTF_8));
            bw.write(fileStr);
            bw.close();
        } catch (Exception e) {
            throw new SqlException(e.getLocalizedMessage());
        }
    }
}
