package com.blacknebula.scrumpoker.enums;


public enum CardSetEnum {
    TIME("time"), FIBONACCI("fibonacci"), MODIFIED_FIBONACCI("modifiedFibonacci"), VOTE("vote");

    private final String value;

    CardSetEnum(String value) {
        this.value = value;
    }

    public static CardSetEnum toEnum(String value) {
        for (CardSetEnum cardSetEnum : CardSetEnum.values()) {
            if (cardSetEnum.getValue().equalsIgnoreCase(value)) {
                return cardSetEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
