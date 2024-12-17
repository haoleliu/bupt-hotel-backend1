package com.haole.bupthotelbackend.model;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Bill {
    private String room_num;

    private LocalDateTime nowTime;

    private LocalDate startTime;

    /**
     *
     */
    private LocalDate endTime;

    /**
     * 入住时长
     */
    private Long totalTime;

    /**
     * 风速
     */
    private Integer speed;

    /**
     * 总费用
     */
    private BigDecimal totalPrice;

    /**
     * 费率
     */
    private BigDecimal rate;

}
