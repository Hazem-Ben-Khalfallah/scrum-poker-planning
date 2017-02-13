package com.blacknebula.scrumpoker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JsonSerializer {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JsonSerializer.class);
    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param object object to serialize to json
     * @return json string
     * @should generate Json String from object
     */
    public static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            LOGGER.error("Error when converting name=" + object.getClass().getName(), ex);
        }
        return "";
    }

    /**
     * @param jsonText json to deserialize
     * @param clazz    created object class
     * @param <T>      object type
     * @return object of type T
     * @should create object for json text
     */
    public static <T> T toObject(String jsonText, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonText, clazz);
        } catch (IOException ex) {
            LOGGER.error("Exception on converting to class=" + clazz.getName(), ex);
            return null;
        }
    }

    /**
     * @param jsonText json list to deserialize
     * @param clazz    created objects class
     * @param <T>      object type
     * @return List of T objects
     * @should create List of objects for json text
     */
    public static <T> List<T> toListObject(String jsonText, Class<T> clazz) {
        try {

            return objectMapper.readValue(jsonText, TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
        } catch (IOException ex) {
            LOGGER.error("Exception on converting to class=" + clazz.getName(), ex);
            return new ArrayList<>();
        }
    }

}
