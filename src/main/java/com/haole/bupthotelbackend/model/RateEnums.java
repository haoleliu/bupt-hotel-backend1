package com.haole.bupthotelbackend.model;

import java.math.BigDecimal;
import java.util.zip.DeflaterOutputStream;

public enum RateEnums {
    LOW(1, 0.3),
    MEDIUM(2, 0.5),
    HIGH(3, 1.0);

    private Integer value;

    private Double rate;


    public static RateEnums getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        RateEnums[] values = RateEnums.values();
        for (RateEnums teamStatusEnum : values) {
            if (teamStatusEnum.getValue() == value) {
                return teamStatusEnum;
            }
        }
        return null;
    }

    RateEnums(int value, Double rate) {
        this.value = value;
        this.rate = rate;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Double getText() {
        return rate;
    }

    public void setText(Double rate) {
        this.rate = rate;
    }
}
