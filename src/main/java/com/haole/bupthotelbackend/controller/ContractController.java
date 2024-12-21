package com.haole.bupthotelbackend.controller;


import com.haole.bupthotelbackend.service.ExcelService;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@CrossOrigin(origins = {"http://localhost:8080","http://82.156.126.178:8080"}, allowCredentials = "true")
@RestController
public class ContractController {


    @Resource
    private ExcelService excelService;

    private String PATH = "F:\\2024Java-Study\\bupt\\bupt_se_hotelSys\\bupt-hotel-backend\\src\\main\\resources\\excels\\";

    @RequestMapping("/download/{room_number}")
    public ResponseEntity<FileSystemResource> downloadContract(@PathVariable Integer room_number) {
        excelService.writeContract(room_number);
        File file = new File(PATH+"room"+room_number+".xlsx");
        FileSystemResource resource = new FileSystemResource(file);

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

}
