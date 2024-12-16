package com.haole.bupthotelbackend.resp;

import com.haole.bupthotelbackend.model.domain.Customer;


import lombok.Data;

import java.io.Serializable;


@Data
public class LoginResp implements Serializable {

    private Integer code;

    private String msg;

    private Customer customer;

}
