package com.idea.plugin.utils;

import com.idea.plugin.sql.support.exception.SqlException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {


    public static List<String> readFile(String path) {
        File file = new File(path);
        List<String> result = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8));
            String s;
            while ((s = br.readLine()) != null) {
                result.add(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void writeFile(String path, String fileStr) throws SqlException {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(fileStr)) {
            return;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true), StandardCharsets.UTF_8));
            bw.write(fileStr);
            bw.close();
        } catch (Exception e) {
            throw new SqlException(e.getLocalizedMessage());
        }
    }

    public static void writeFileDelete(String path, String fileStr) throws SqlException {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        writeFile(path, fileStr);
    }


    public static void copyFile(String path, String newPath) throws IOException {
        File newfile = new File(newPath);
        if (newfile.exists()) {
            newfile.delete();
        }
        File file = new File(path);
        org.apache.commons.io.FileUtils.copyFile(file, newfile);
    }
}
