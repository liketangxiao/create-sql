package com.idea.plugin.translator;

import com.google.common.collect.ImmutableMap;
import com.idea.plugin.translator.impl.BaiduTranslator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TranslatorConfig {
    private static Map<String, Translator> translatorMap = ImmutableMap.<String, Translator>builder()
            .put("BAIDU_TRANSLATOR", new BaiduTranslator())
            .build();

    /**
     * 自动翻译
     *
     * @param source 源
     * @return {@link String}
     */
    public static String translate(String source) {
        Translator translator = translatorMap.get("BAIDU_TRANSLATOR");
        if (Objects.isNull(translator)) {
            return StringUtils.EMPTY;
        }
        return translator.en2Ch(StringUtils.join(split(source), StringUtils.SPACE));
    }

    private static List<String> split(String word) {
        word = word.replaceAll("(?<=[^A-Z])[A-Z][^A-Z]", "_$0");
        word = word.replaceAll("[A-Z]{2,}", "_$0");
        word = word.replaceAll("(^[A-Z]){1}[_]{1}", "");
        word = word.replaceAll("_+", "_");
        return Arrays.stream(word.split("_")).map(String::toLowerCase).collect(Collectors.toList());
    }
}
