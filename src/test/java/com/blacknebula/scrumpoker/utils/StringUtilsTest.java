package com.blacknebula.scrumpoker.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author hazem
 */
public class StringUtilsTest {
    /**
     * @verifies return true if value is null or empty
     * @see StringUtils#isEmpty(String, boolean)
     */
    @Test
    public void isEmpty_shouldReturnTrueIfValueIsNullOrEmpty() throws Exception {
        Assertions.assertThat(StringUtils.isEmpty(null, false)).isTrue();
        Assertions.assertThat(StringUtils.isEmpty("", false)).isTrue();
    }

    /**
     * @verifies return true if value contains spaces and trim is true
     * @see StringUtils#isEmpty(String, boolean)
     */
    @Test
    public void isEmpty_shouldReturnTrueIfValueContainsSpacesAndTrimIsTrue() throws Exception {
        Assertions.assertThat(StringUtils.isEmpty("    ", true)).isTrue();
    }

    /**
     * @verifies return false if value contains spaces and trim is false
     * @see StringUtils#isEmpty(String, boolean)
     */
    @Test
    public void isEmpty_shouldReturnFalseIfValueContainsSpacesAndTrimIsFalse() throws Exception {
        Assertions.assertThat(StringUtils.isEmpty("    ", false)).isFalse();
    }
}
