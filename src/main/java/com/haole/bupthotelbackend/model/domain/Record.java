package com.haole.bupthotelbackend.model.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Record {
    @ExcelProperty("空调费用")
    private BigDecimal acFee;

    @ExcelProperty("空调使用时间")
    private BigDecimal acUsageTime;

    @ExcelProperty("当前温度")
    private BigDecimal currentTemperature;

    @ExcelProperty("风速")
    private int speed;

    @ExcelProperty("设定温度")
    private BigDecimal temperature;

    @ExcelProperty("时间戳")
    private Date timestamp;
}
