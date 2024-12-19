package com.haole.bupthotelbackend.request;

import lombok.Data;

import java.time.LocalDate;


@Data
public class CheckInRequest {
    private Integer roomNumber;
    private String name;
    private String phone;
    private String idCard;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
