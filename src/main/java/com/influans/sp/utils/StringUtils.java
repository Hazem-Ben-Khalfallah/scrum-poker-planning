package com.influans.sp.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils extends org.springframework.util.StringUtils {
    protected static Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static boolean isEmpty(String value, boolean trim) {
        return value == null || (trim ? value.trim().length() == 0 : value.length() == 0);
    }

}
