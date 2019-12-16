package com.blacknebula.scrumpoker.dto;

import java.util.HashMap;

/**
 * @author hazem
 */
public class ErrorResponse extends HashMap<String, String> {
    public static class Attributes {
        final public static String EXCEPTION = "exception";
        final public static String DATE_TIME = "datetime";
        final public static String URI = "uri";
    }
}
