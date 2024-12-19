package com.haole.bupthotelbackend.model;

public enum SpeedEnums {
    LOW(1, "低速"),
    MEDIUM(2, "中速"),
    HIGH(3, "高速");

    private Integer value;

    private String text;


    public static SpeedEnums getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        SpeedEnums[] values = SpeedEnums.values();
        for (SpeedEnums teamStatusEnum : values) {
            if (teamStatusEnum.getValue() == value) {
                return teamStatusEnum;
            }
        }
        return null;
    }

    SpeedEnums(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
