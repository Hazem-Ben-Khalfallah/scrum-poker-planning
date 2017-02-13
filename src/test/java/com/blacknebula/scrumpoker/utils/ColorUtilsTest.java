package com.blacknebula.scrumpoker.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author hazem
 */
public class ColorUtilsTest {
    /**
     * @verifies return a color code
     * @see ColorUtils#getRandomColor()
     */
    @Test
    public void getRandomColor_shouldReturnAColorCode() throws Exception {
        final String color = ColorUtils.getRandomColor();
        Assertions.assertThat(color).isNotNull();
        Assertions.assertThat(color.length()).isEqualTo(7);
        Assertions.assertThat(color).startsWith("#");
    }

    /**
     * @verifies return different color code when called twice
     * @see ColorUtils#getRandomColor()
     */
    @Test
    public void getRandomColor_shouldReturnDifferentColorCodeWhenCalledTwice() throws Exception {
        final String color1 = ColorUtils.getRandomColor();
        final String color2 = ColorUtils.getRandomColor();
        Assertions.assertThat(color1).isNotNull();
        Assertions.assertThat(color2).isNotNull();
        Assertions.assertThat(color1).isNotEqualTo(color2);
    }
}
