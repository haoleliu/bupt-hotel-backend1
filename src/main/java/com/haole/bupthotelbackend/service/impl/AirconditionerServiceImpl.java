package com.haole.bupthotelbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haole.bupthotelbackend.model.StatusRsp;
import com.haole.bupthotelbackend.model.domain.Airconditioner;
import com.haole.bupthotelbackend.model.domain.Room;
import com.haole.bupthotelbackend.request.UpdateRequest;
import com.haole.bupthotelbackend.service.AirconditionerService;
import com.haole.bupthotelbackend.mapper.AirconditionerMapper;
import com.haole.bupthotelbackend.service.RoomService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author liu haole
 * @description 针对表【airconditioner】的数据库操作Service实现
 * @createDate 2024-12-15 14:57:28
 */

@Slf4j
@Service
public class AirconditionerServiceImpl extends ServiceImpl<AirconditionerMapper, Airconditioner>
        implements AirconditionerService {


    @Resource
    private RoomService roomService;


    @Override
    public Boolean updateAirconditioner(Long room_number, UpdateRequest request) {
        Airconditioner trueAirconditioner = this.lambdaQuery().eq(Airconditioner::getId, room_number).one();

        Airconditioner airconditioner = new Airconditioner();
        airconditioner.setPower(request.getPower());
        airconditioner.setId(room_number);
        airconditioner.setTemperature(request.getTemperature());
        airconditioner.setMode(request.getMode());
        airconditioner.setSpeed(request.getSpeed());
        airconditioner.setQueue(trueAirconditioner.getQueue());

        QueryWrapper<Airconditioner> wrapper = new QueryWrapper<>();
        wrapper.eq("id", room_number);
        Airconditioner airconditioner1 = this.getOne(wrapper);
        if (airconditioner1 == null) {
            return false;
        }
        Room room = roomService.lambdaQuery().eq(Room::getRoomNumber, room_number).one();
        if (airconditioner1.getPower() == 1 && request.getPower() == 0) {
            //将费用计入
            int rate = airconditioner1.getSpeed();
            LocalDateTime nowTime = LocalDateTime.now();
            Date lastStartTime = airconditioner1.getLastStartTime();
            LocalDateTime lastAcUseTime = LocalDateTime.ofInstant(lastStartTime.toInstant(), ZoneId.systemDefault());
            Duration duration = Duration.between(lastAcUseTime, nowTime);
            //风速n，就是一分钟n度电，费率是一度电一块钱。
            log.info("原空调使用时间:{}",room.getAcUsageTime());
            long minutes = duration.toMinutes();
            BigDecimal fee = BigDecimal.valueOf(minutes * rate);
            room.setAcFee(room.getAcFee().add(fee));
            room.setAcUsageTime((int)(room.getAcUsageTime()+minutes));
            log.info("空调费用为{},持续使用时间为{}",fee, duration);
            roomService.lambdaUpdate()
                    .eq(Room::getRoomNumber, room_number)
                    .update(room);
        }
        this.lambdaUpdate()
                .eq(Airconditioner::getId, room_number)
                .update(airconditioner);
        return true;
    }

    @Override
    public StatusRsp getAirconditioner(Long room_number) {

        Airconditioner airconditioner = this.lambdaQuery().eq(Airconditioner::getId, room_number).one();
        Room room=roomService.lambdaQuery().eq(Room::getRoomNumber,room_number).one();

        if (airconditioner == null) {
            return null;
        }
        StatusRsp result = new StatusRsp();
        result.setCost(room.getAcFee());
        result.setQueue(airconditioner.getQueue());
        result.setTime(room.getAcUsageTime());
        return result;
    }
}




