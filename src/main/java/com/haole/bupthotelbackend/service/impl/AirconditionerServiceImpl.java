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
        airconditioner.setPower(request.getPower() ? 1 : 0);
        airconditioner.setId(room_number);
        airconditioner.setTemperature(request.getTemperature());
        airconditioner.setMode(request.getMode());
        airconditioner.setSpeed(request.getSpeed());
        airconditioner.setQueue(trueAirconditioner.getQueue());
        airconditioner.setLastStartTime(new Date());

        QueryWrapper<Airconditioner> wrapper = new QueryWrapper<>();
        wrapper.eq("id", room_number);
        Airconditioner airconditioner1 = this.getOne(wrapper);
        if (airconditioner1 == null) {
            return false;
        }
        Room room = roomService.lambdaQuery().eq(Room::getRoomNumber, room_number).one();
        if (airconditioner1.getPower() == 1 && request.getPower() == false) {
            //将费用计入
            int rate = airconditioner1.getSpeed();
            LocalDateTime nowTime = LocalDateTime.now();
            Date lastStartTime = airconditioner1.getLastStartTime();
            LocalDateTime lastAcUseTime = LocalDateTime.ofInstant(lastStartTime.toInstant(), ZoneId.systemDefault());
            Duration duration = Duration.between(lastAcUseTime, nowTime);
            //风速n，就是一分钟n度电，费率是一度电一块钱。
            log.info("原空调使用时间:{}", room.getAcUsageTime());
            long seconds = duration.toSeconds();
            BigDecimal fee = BigDecimal.valueOf(1.0 * seconds / 60 * rate);
            room.setAcFee(room.getAcFee().add(fee));
            room.setAcUsageTime(room.getAcUsageTime().add(BigDecimal.valueOf(1.0 * seconds * 60)));
            log.info("空调费用为{},持续使用时间为{}", fee, duration);
            roomService.lambdaUpdate()
                    .eq(Room::getRoomNumber, room_number)
                    .update(room);
        }
        this.lambdaUpdate()
                .eq(Airconditioner::getId, room_number)
                .update(airconditioner);
        log.info("空调状态更新成功,request:{},id:{},temperature:{},mode:{}",
                request,
                room_number,
                request.getTemperature(),
                request.getMode()
        );
        return true;
    }

    @Override
    public StatusRsp getAirconditioner(Long room_number) {

        Airconditioner airconditioner = this.lambdaQuery().eq(Airconditioner::getId, room_number).one();
        Room room = roomService.lambdaQuery().eq(Room::getRoomNumber, room_number).one();

        if (airconditioner == null) {
            return null;
        }
        if (airconditioner.getPower() == 1) {
            //空调在使用过程中，实时计算费用
            LocalDateTime nowTime = LocalDateTime.now();
            Date lastStartTime = airconditioner.getLastStartTime();
            LocalDateTime lastAcUseTime = LocalDateTime.ofInstant(lastStartTime.toInstant(), ZoneId.systemDefault());
            Duration duration = Duration.between(lastAcUseTime, nowTime);
            //计算空调距离数据库中的lastUseTime到现在的时间差
            long seconds = duration.toSeconds();
            log.info("距离上次使用的秒数:{}", seconds);
            BigDecimal fee = BigDecimal.valueOf(1.0 * seconds * airconditioner.getSpeed() / 60);
            //room更新费用
            log.info("房间状态:{}", room);
            room.setAcFee(room.getAcFee().add(fee));
            room.setAcUsageTime(room.getAcUsageTime().add(BigDecimal.valueOf(1.0 * seconds / 60)));
            room.setTotalFee(room.getTotalFee().add(fee));
            roomService.lambdaUpdate().eq(Room::getRoomNumber, room_number).update(room);
            //airconditioner更新使用时间
            airconditioner.setLastStartTime(new Date());
            log.info("新的空调状态：{}", airconditioner);
            this.lambdaUpdate().eq(Airconditioner::getId, room_number).update(airconditioner);
        }
        StatusRsp result = new StatusRsp();
        result.setPower((airconditioner.getPower() == 1));
        result.setCost(room.getAcFee());
        result.setQueue(airconditioner.getQueue());
        result.setTime(room.getAcUsageTime());
        return result;
    }
}




