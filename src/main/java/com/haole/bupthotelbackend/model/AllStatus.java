package com.haole.bupthotelbackend.model;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class AllStatus {

    private int roomNumber;

    private boolean power;

    private String mode;

    private String speed;

    private BigDecimal currentTemperature;

    private BigDecimal targetTemperature;

    private BigDecimal time;

    private BigDecimal cost;

    private String queue;

}