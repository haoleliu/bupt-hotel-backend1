package com.haole.bupthotelbackend.excelText;


import com.haole.bupthotelbackend.service.ExcelService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class excelTest {

    @Resource
    private ExcelService excelService;


    @Test
    public void excelTest(){
        Integer room_num=4;
        excelService.writeContract(room_num);
        
    }



}
