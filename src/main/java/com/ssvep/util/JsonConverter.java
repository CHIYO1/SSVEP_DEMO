/**
 * 这个文件包含一个工具类，
 * 用于将 Java 中的 Map<String, Object> 类型与 JSON 字符串进行相互转换。
 * 
 * @author 石振山
 * @version 2.0.0
 */
package com.ssvep.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将 Map<String, Object> 转换为 JSON 字符串
     * 
     * @param map 要转换的 Map
     * @return JSON 格式的字符串
     */
    public static String convertToJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert map to JSON string.", e);
        }
    }

    /**
     * 将 JSON 字符串转换为 Map<String, Object>
     * 
     * @param json 要转换的 JSON 字符串
     * @return Map<String, Object> 对象
     */
    public static Map<String, Object> convertToMap(String json) {
        try {
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class));
        } catch (IOException e) {
            throw new RuntimeException("Could not convert JSON string to map.", e);
        }
    }
}