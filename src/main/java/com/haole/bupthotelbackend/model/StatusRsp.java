package com.haole.bupthotelbackend.model;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StatusRsp implements Serializable {

    private Boolean power;

    private BigDecimal cost;

    private String queue;

    private BigDecimal time;

}
