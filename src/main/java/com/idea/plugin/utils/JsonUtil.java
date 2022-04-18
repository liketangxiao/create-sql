package com.idea.plugin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * @author wangchao
 * @date 2019/09/01
 */
public class JsonUtil {
    private static final Logger LOGGER = Logger.getInstance(JsonUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {
    }

    public static <T> String toJson(T obj) {
        if (Objects.isNull(obj)) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.warn("json序列化异常", e);
            return "";
        }
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            LOGGER.warn("json序列化异常,json=" + json, e);
            return null;
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> tTypeReference) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, tTypeReference);
        } catch (IOException e) {
            LOGGER.warn("json序列化异常,json=" + json, e);
            return null;
        }
    }

}
