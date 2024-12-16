package com.haole.bupthotelbackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haole.bupthotelbackend.model.domain.Airconditioner;
import com.haole.bupthotelbackend.model.domain.Customer;
import com.haole.bupthotelbackend.request.LoginRequest;
import com.haole.bupthotelbackend.resp.LoginResp;
import com.haole.bupthotelbackend.service.AirconditionerService;
import com.haole.bupthotelbackend.service.CustomerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RestController
public class AirconditionController {

    @Resource
    private CustomerService customerService;

    @Resource
    private AirconditionerService airconditionerService;

    @RequestMapping("/customer-login/")
    public LoginResp login(@RequestBody LoginRequest request){
        LoginResp loginResp = new LoginResp();
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_number_id", request.getRoom_number());
        try {
            Customer result = customerService.getOne(queryWrapper);
            loginResp.setCode(200);
            loginResp.setMsg("查询成功");
            loginResp.setCustomer(result);
        }catch (Exception e){
            loginResp.setCode(400);
            loginResp.setMsg("查询失败");
        }
        return loginResp;
    }

    @RequestMapping("/getData")
    public List<Airconditioner> getData(){
        return airconditionerService.list();
    }


}
