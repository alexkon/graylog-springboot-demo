package com.example.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String jsonToPrettyString(Object node) {
        try {
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            logger.error("Exception during parsing JSON: {}", e.getMessage());
            return "{ \"error\": \"JsonProcessingException\" }";
        }
    }

    public static JsonNode mapToJsonNode(Map map) {
        return jsonMapper.convertValue(map, JsonNode.class);
    }
}
