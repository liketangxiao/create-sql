package com.idea.plugin.orm.service;

import com.idea.plugin.sql.support.exception.SqlException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GeneratorConfig {

    private static final String ENCODING = "UTF-8";

    private static FreemarkerConfiguration freemarker = new FreemarkerConfiguration("/template");

    protected Template getTemplate(String ftl) throws IOException {
        return freemarker.getTemplate(ftl, ENCODING);
    }

    static class FreemarkerConfiguration extends Configuration {

        public FreemarkerConfiguration(String basePackagePath) {
            super(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            setDefaultEncoding(ENCODING);
            setClassForTemplateLoading(getClass(), basePackagePath);
        }

    }

    public void writeFile(String path, String fileStr) throws SqlException {
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
