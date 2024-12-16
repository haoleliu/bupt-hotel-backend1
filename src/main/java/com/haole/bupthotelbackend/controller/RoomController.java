package com.haole.bupthotelbackend.controller;


import com.haole.bupthotelbackend.model.domain.Customer;
import com.haole.bupthotelbackend.model.domain.Room;
import com.haole.bupthotelbackend.request.CheckInRequest;
import com.haole.bupthotelbackend.service.CustomerService;
import com.haole.bupthotelbackend.service.RoomService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RestController
public class RoomController {

    @Resource
    private RoomService roomService;

    private CustomerService customerService;

    @RequestMapping("/getRoom")
    public Room getRoomByNo(Integer room_No) {
        return roomService.getRoomByNo(room_No);
    }

    @RequestMapping("/check-in/")
    public boolean checkIn(@RequestBody CheckInRequest request) {
        boolean result = roomService.checkIn(
                request.getRoomNumber(),
                request.getName(),
                request.getCheckInDate(),
                request.getCheckOutDate());
        log.info("checkIn: room_No={}, name={}, checkInTime={}, checkOutTime={}",
                request.getRoomNumber(), request.getName(), request.getCheckInDate(), request.getCheckOutDate());
        return result;
    }

    @RequestMapping("/checkOut")
    public boolean checkOut(@RequestParam Integer room_No) {
        Room result = roomService.lambdaQuery().eq(Room::getRoomNumber, room_No).one();
        if (result == null) {
            log.info("checkOut: room_No={}, result=null");
            return false;
        }
        System.out.println();
        roomService.lambdaUpdate().eq(Room::getRoomNumber, room_No)
                .set(Room::getCheckInDate, null)
                .set(Room::getCheckOutDate, null)
                .set(Room::getIsOccupied, 0)
                .update();
        customerService.lambdaUpdate()
                .eq(Customer::getRoomNumberId, room_No)
                .set(Customer::getRoomNumberId, null);
        return true;
    }


}
