package com.haole.bupthotelbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haole.bupthotelbackend.model.Bill;
import com.haole.bupthotelbackend.model.domain.Customer;
import com.haole.bupthotelbackend.model.domain.Room;
import com.haole.bupthotelbackend.service.CustomerService;
import com.haole.bupthotelbackend.service.RoomService;
import com.haole.bupthotelbackend.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
* @author liu haole
* @description 针对表【room】的数据库操作Service实现
* @createDate 2024-12-15 14:57:28
*/
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
    implements RoomService{

    private CustomerService customerService;

    @Override
    public Room getRoomByNo(int room_No) {
        return this.lambdaQuery().eq(Room::getRoomNumber,room_No).one();
    }

    @Override
    public boolean checkIn(Integer roomNo, String name, LocalDate checkInTime, LocalDate checkOutTime) {
        Room room = this.getRoomByNo(roomNo);
        if (room.getIsOccupied() == 1) {
            return false;
        }
        UpdateWrapper<Room> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("room_number", roomNo)
                .set("is_occupied", 1)
                .set("check_in_date", checkInTime)
                .set("check_out_date", checkOutTime)
                .set("total_fee", 0);
        customerService.lambdaUpdate().eq(Customer::getName, name).set(Customer::getRoomNumberId, roomNo);
        return true;
    }

    @Override
    public Bill showBill(Integer roomNo) {
        Bill bill = new Bill();
        /**
         * 风速1,2,3对应费率为一小时1，2，3元
         * 房间费用12345分别为100，125，150，200，100每天
         */
        Room room = this.getRoomByNo(roomNo);
        return null;
    }
}




