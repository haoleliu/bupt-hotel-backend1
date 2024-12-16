package com.haole.bupthotelbackend.request;


import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {

    private String room_number;

}
