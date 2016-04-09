package com.influans.sp.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Optional;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JsonSerializer {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JsonSerializer.class);
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            LOGGER.error("Error when converting name=" + object.getClass().getName(), ex);
        }
        return "";
    }

    public static <T> T toObject(String jsonText, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonText, clazz);
        } catch (IOException ex) {
            LOGGER.error("Exception on converting to class=" + clazz.getName(), ex);
            return null;
        }
    }

    public static <T> List<T> toListObject(String jsonText, Class<T> clazz) {
        try {

            return objectMapper.readValue(jsonText, TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
        } catch (IOException ex) {
            LOGGER.error("Exception on converting to class=" + clazz.getName(), ex);
            return new ArrayList<>();
        }
    }

    public static Optional<JsonNode> parse(String jsonText) {
        try {
            return Optional.of(objectMapper.readTree(jsonText));
        } catch (IOException ex) {
            LOGGER.error("Exception on converting value=" + jsonText, ex);
            return Optional.absent();
        }
    }
}
