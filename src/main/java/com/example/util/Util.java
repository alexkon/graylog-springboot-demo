package com.example.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static String jsonToPrettyString(ObjectNode node) {
        try {
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            logger.error("Exception during parsing JSON: {}", e.getMessage());
            return "{ \"error\": \"JsonProcessingException\" }";
        }
    }
}
