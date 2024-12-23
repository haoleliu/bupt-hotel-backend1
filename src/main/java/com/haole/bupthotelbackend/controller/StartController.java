package com.haole.bupthotelbackend.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartController {

    @RequestMapping("/start")
    private Boolean start() throws InterruptedException {
        for (int i = 0; i < 25; i++) {

        }
        Thread.sleep(1000);


        return true;
    }




}
