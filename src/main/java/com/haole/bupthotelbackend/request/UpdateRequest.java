package com.haole.bupthotelbackend.request;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UpdateRequest implements Serializable {

    private BigDecimal temperature;

    private String mode;

    private Boolean power;

    private Integer speed;

}
