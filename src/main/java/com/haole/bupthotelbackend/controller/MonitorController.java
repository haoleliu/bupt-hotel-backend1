package com.haole.bupthotelbackend.controller;


import com.haole.bupthotelbackend.model.AllStatus;
import com.haole.bupthotelbackend.model.SpeedEnums;
import com.haole.bupthotelbackend.model.domain.Airconditioner;
import com.haole.bupthotelbackend.model.domain.Room;
import com.haole.bupthotelbackend.model.domain.Waitqueue;
import com.haole.bupthotelbackend.service.AirconditionerService;
import com.haole.bupthotelbackend.service.RoomService;
import com.haole.bupthotelbackend.service.WaitqueueService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.haole.bupthotelbackend.model.SpeedEnums.LOW;


@RestController
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class MonitorController {

    @Resource
    private AirconditionerService airconditionerService;

    @Resource
    private RoomService roomService;

    @Resource
    private WaitqueueService waitqueueService;

    @RequestMapping("/monitor/{room_num}/all")
    public AllStatus monitor(@PathVariable Integer room_num) {
        AllStatus result = new AllStatus();
        result.setRoomNumber(room_num);
        Room room = roomService.getRoomByNo(room_num);
        Airconditioner airconditioner = airconditionerService.lambdaQuery().eq(Airconditioner::getRoomId, room_num).one();
        Waitqueue waitqueueThis = waitqueueService.lambdaQuery().eq(Waitqueue::getRoomNum, room_num).one();
        result.setRoomNumber(room_num);
        result.setPower(airconditioner.getPower()==1);
        result.setMode(airconditioner.getMode());
        result.setSpeed(SpeedEnums.getEnumByValue(airconditioner.getSpeed()).getText());
        result.setCurrentTemperature(room.getCurrentTemperature());
        result.setTargetTemperature(airconditioner.getTemperature());
        result.setTime(room.getAcUsageTime());
        result.setCost(room.getAcFee());
        //返回1就是在服务中
        if (airconditioner.getPower()==1){
            result.setQueue(String.valueOf(1));
        }
        else if (waitqueueThis.getIsWaiting()==1){
            result.setQueue(String.valueOf(0));
        }
        else {
            result.setQueue(String.valueOf(-1));
        }
        return result;
    }


}
