package com.blacknebula.scrumpoker.utils;


public class StringUtils extends org.springframework.util.StringUtils {
    /**
     * @param value String value
     * @param trim  if extra spaces should be removed before verification
     * @return true is empty or null.
     * @should return true if value is null or empty
     * @should return true if value contains spaces and trim is true
     * @should return false if value contains spaces and trim is false
     */
    public static boolean isEmpty(String value, boolean trim) {
        return value == null || (trim ? value.trim().length() == 0 : value.length() == 0);
    }

}
