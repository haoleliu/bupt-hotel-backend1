package com.haole.bupthotelbackend.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.ListUtils;
import com.haole.bupthotelbackend.model.domain.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class ContractData {
    @ExcelProperty("房间号")
    private String room_number;
    @ExcelProperty("入住人")
    private String name;
    @ExcelProperty("身份证号")
    private String Id_Card;
    @ExcelProperty("入住时间")
    private String checkin_date;
    @ExcelProperty("退房时间")
    private String checkout_date;
    @ExcelProperty("空调费用")
    private BigDecimal acFee;
    @ExcelProperty("总费用")
    private BigDecimal totalFee;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;

}