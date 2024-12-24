package com.haole.bupthotelbackend.service;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public interface ExcelService {

    public void writeContract(Integer room_number);

}
