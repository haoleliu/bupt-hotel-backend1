package com.haole.bupthotelbackend.controller;


import com.haole.bupthotelbackend.model.Bill;
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
@CrossOrigin(origins = {"http://localhost:8080","http://82.156.126.178:8080"}, allowCredentials = "true")
@RestController
public class RoomController {

    @Resource
    private RoomService roomService;

    @Resource
    private CustomerService customerService;

    @RequestMapping("/getRoom")
    public Room getRoomByNo(Integer room_No) {
        return roomService.getRoomByNo(room_No);
    }

    @RequestMapping("/check-in/")
    public Room checkIn(@RequestBody CheckInRequest request) {
        Customer one = customerService.lambdaQuery()
                .eq(Customer::getRoomNumberId, request.getRoomNumber())
                .eq(Customer::getIsIn, 1)
                .one();
        if (one != null) {
            log.info("房间已被入住");
            return null;
        }
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setIdCard(request.getIdCard());
        customer.setRoomNumberId(String.valueOf(request.getRoomNumber()));
        customerService.save(customer);
        log.info("顾客信息：{}", customer);
        Room result = roomService.checkIn(
                request.getRoomNumber(),
                request.getName(),
                request.getCheckInDate(),
                request.getCheckOutDate());
        log.info("checkIn: room_No={}, name={}, checkInTime={}, checkOutTime={}",
                request.getRoomNumber(), request.getName(), request.getCheckInDate(), request.getCheckOutDate());
        return result;
    }

    @RequestMapping("/checkout/room/{room_No}")
    public boolean checkOut(@PathVariable Integer room_No) {
        log.info("登出房间号:{}",room_No);
        String roomNum=String.valueOf(room_No);
        Room result = roomService.lambdaQuery().eq(Room::getRoomNumber, roomNum).one();
        if (result == null) {
            log.info("checkOut: room_No={}, result=null");
            return false;
        }
        System.out.println();
        roomService.lambdaUpdate()
                .eq(Room::getRoomNumber, roomNum)
                .set(Room::getCheckInDate, null)
                .set(Room::getCheckOutDate, null)
                .set(Room::getIsOccupied, 0)
                .update();
        customerService.lambdaUpdate()
                .eq(Customer::getRoomNumberId, roomNum)
                .set(Customer::getIsIn, 0)
                .update();
        return true;
    }


    /**
     * 出示详单
     */
    @RequestMapping("/showBill")
    public Bill showBill(@RequestParam Integer room_No) {

        return roomService.showBill(room_No);
    }


}
