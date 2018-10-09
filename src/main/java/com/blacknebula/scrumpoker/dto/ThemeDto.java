package com.blacknebula.scrumpoker.dto;

/**
 * @author hazem
 */
public class ThemeDto {
    private String cardTheme;

    public ThemeDto() {
    }

    private ThemeDto(Builder builder) {
        setCardTheme(builder.cardTheme);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getCardTheme() {
        return cardTheme;
    }

    public void setCardTheme(String cardTheme) {
        this.cardTheme = cardTheme;
    }


    public static final class Builder {
        private String cardTheme;

        private Builder() {
        }

        public Builder cardTheme(String cardTheme) {
            this.cardTheme = cardTheme;
            return this;
        }

        public ThemeDto build() {
            return new ThemeDto(this);
        }
    }
}
