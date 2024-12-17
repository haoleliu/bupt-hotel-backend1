package com.haole.bupthotelbackend.service;

import com.haole.bupthotelbackend.model.Bill;
import com.haole.bupthotelbackend.model.domain.Room;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* @author liu haole
* @description 针对表【room】的数据库操作Service
* @createDate 2024-12-15 14:57:28
*/
public interface RoomService extends IService<Room> {
    Room getRoomByNo(int room_No);


    boolean checkIn(Integer roomNo, String name, LocalDate checkInTime, LocalDate checkOutTime);

    Bill showBill(Integer roomNo);
}
