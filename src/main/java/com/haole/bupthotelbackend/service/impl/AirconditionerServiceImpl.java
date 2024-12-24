package com.haole.bupthotelbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haole.bupthotelbackend.model.RateEnums;
import com.haole.bupthotelbackend.model.SpeedEnums;
import com.haole.bupthotelbackend.model.StatusRsp;
import com.haole.bupthotelbackend.model.domain.Airconditioner;
import com.haole.bupthotelbackend.model.domain.Room;
import com.haole.bupthotelbackend.model.domain.Waitqueue;
import com.haole.bupthotelbackend.request.UpdateRequest;
import com.haole.bupthotelbackend.service.AirconditionerService;
import com.haole.bupthotelbackend.mapper.AirconditionerMapper;
import com.haole.bupthotelbackend.service.RoomService;
import com.haole.bupthotelbackend.service.WaitqueueService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.Wait;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Resource
    private WaitqueueService waitqueueService;

    @Override
    public Boolean updateAirconditioner(Long room_number, UpdateRequest request) {
        Boolean result = true;
        List<Airconditioner> airconditionersList = this.list();
        int inUsingNum = 0;
        Waitqueue waitqueueThis = waitqueueService.lambdaQuery().eq(Waitqueue::getRoomNum, room_number).one();
        //找waitqueue is waiting
        for (Airconditioner airconditioner : airconditionersList) {
            if (airconditioner.getPower() == 1) {
                inUsingNum++;
            }
        }
        //如果使用队列满了，加入等待队列中
        if (inUsingNum >= 3 && request.getPower() == true) {
            result = false;
            request.setPower(false);
            waitqueueService.lambdaUpdate()
                    .eq(Waitqueue::getRoomNum, room_number)
                    .set(Waitqueue::getLastRequestTime, new Date())
                    .set(Waitqueue::getWaitingTime, BigDecimal.valueOf(0))
                    .set(Waitqueue::getIsWaiting, 1)
                    .update();
            waitqueueThis.setIsWaiting(1);
            this.lambdaUpdate().eq(Airconditioner::getRoomId, room_number)
                    .set(Airconditioner::getQueue, String.valueOf(waitqueueThis.getIsWaiting()))
                    .update();
        }
        Airconditioner airconditioner = new Airconditioner();
        airconditioner.setPower(request.getPower() ? 1 : 0);
        airconditioner.setId(room_number);
        airconditioner.setTemperature(request.getTemperature());
        airconditioner.setMode(request.getMode());
        airconditioner.setSpeed(request.getSpeed());
        airconditioner.setQueue(String.valueOf(waitqueueThis.getIsWaiting()));
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
            BigDecimal fee = BigDecimal.valueOf(1.0 * seconds / 60 * rate / 3);
            room.setAcFee(room.getAcFee().add(fee));
            room.setAcUsageTime(room.getAcUsageTime().add(BigDecimal.valueOf(1.0 * seconds / 60 / 3)));
            if (room.getCurrentTemperature().compareTo(airconditioner.getTemperature()) < 0) {
                //房间温度小于设定温度
                room.setCurrentTemperature(room.getCurrentTemperature().add(BigDecimal.valueOf(seconds / 60)));
            } else {
                //房间温度大于设
                room.setCurrentTemperature(room.getCurrentTemperature().subtract(BigDecimal.valueOf(seconds / 60)));
            }
            room.setCurrentTemperature(room.getCurrentTemperature().add(BigDecimal.valueOf(seconds / 60)));
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
        return result;
    }

    @Override
    public StatusRsp getAirconditioner(Long room_number) {
        int totalUsingNum = 0;
        Airconditioner airconditioner = this.lambdaQuery().eq(Airconditioner::getId, room_number).one();
        Room room = roomService.lambdaQuery().eq(Room::getRoomNumber, room_number).one();
        Waitqueue waitqueueThis = waitqueueService.lambdaQuery().eq(Waitqueue::getRoomNum, room_number).one();
        if (waitqueueThis.getIsWaiting() == 1) {
            //如果在等待中，更新等待时间
            Date lastRequestTime = waitqueueThis.getLastRequestTime();
            LocalDateTime nowTime = LocalDateTime.now();
            LocalDateTime lastRequestTimeLocal = LocalDateTime.ofInstant(lastRequestTime.toInstant(), ZoneId.systemDefault());
            Duration duration = Duration.between(lastRequestTimeLocal, nowTime);
            long seconds = duration.toSeconds();
            waitqueueService.lambdaUpdate().eq(Waitqueue::getRoomNum, room_number)
                    .set(Waitqueue::getWaitingTime, waitqueueThis.getWaitingTime().add(BigDecimal.valueOf(seconds)))
                    .set(Waitqueue::getLastRequestTime, new Date())
                    .update();
        }
        List<Airconditioner> airconditionerList = this.lambdaQuery().list();
        int rate = airconditioner.getSpeed();
        //如果使用空调数小于3，则可以将等待队列中的空调启动
        for (Airconditioner airconditioner1 : airconditionerList) {
            if (airconditioner1.getPower() == 1) {
                totalUsingNum++;
            }
        }
        List<Waitqueue> list = waitqueueService.lambdaQuery().eq(Waitqueue::getIsWaiting, 1)
                .orderBy(true, false, Waitqueue::getLastRequestTime)
                .list();
        List<Airconditioner> usingACList = this.lambdaQuery()
                .eq(Airconditioner::getPower, 1)
                .orderBy(true, false, Airconditioner::getAcUsageTime)
                .list();
        String alterRoomNumber = null;
        if (usingACList.size() > 0) {
            Airconditioner alterAC = usingACList.get(0);
            alterRoomNumber = alterAC.getRoomId();
        }
        int size = list.size();//等待中的空调数量
        if (totalUsingNum < 3 && size > 0) {
            int canUseNum = 3 - totalUsingNum;
            log.info("等待队列中的空调(按照上次调用时间降序):{}", list);
            for (int i = 0; i < Math.min(canUseNum, size); i++) {
                int num = list.get(i).getRoomNum();
                //将等待时间超过20s的空调开启，并且移出等待队列
                this.lambdaUpdate().eq(Airconditioner::getId, num)
                        .set(Airconditioner::getPower, 1)
                        .update();
                waitqueueService.lambdaUpdate().eq(Waitqueue::getRoomNum, num)
                        .set(Waitqueue::getIsWaiting, 0)
                        .set(Waitqueue::getWaitingTime, BigDecimal.valueOf(0))
                        .update();
            }
        }
        //如果超过三个空调在使用中，需要一个调度算法，找到等待时间超过20s的空调，换掉总使用时间最长的空调
        if (totalUsingNum == 3 && size > 0) {
            for (Waitqueue waitRoom : list) {
                if (waitRoom.getWaitingTime().compareTo(BigDecimal.valueOf(20)) > 0) {
                    //找到等待时间超过20s的空调，开启
                    int num = waitRoom.getRoomNum();
                    this.lambdaUpdate().eq(Airconditioner::getId, num)
                            .set(Airconditioner::getPower, 1)
                            .set(Airconditioner::getQueue, 0)
                            .update();
                    waitqueueService.lambdaUpdate().eq(Waitqueue::getRoomNum, num)
                            .set(Waitqueue::getIsWaiting, 0)
                            .set(Waitqueue::getWaitingTime, BigDecimal.valueOf(0))
                            .set(Waitqueue::getLastRequestTime, new Date())
                            .set(Waitqueue::getWaitingTime, 0)
                            .update();
                    //将使用时间最长的空调，关闭
                    this.lambdaUpdate().eq(Airconditioner::getId, alterRoomNumber)
                            .set(Airconditioner::getPower, 0)
                            .set(Airconditioner::getQueue, 1)
                            .update();
                    waitqueueService.lambdaUpdate().eq(Waitqueue::getRoomNum, alterRoomNumber)
                            .set(Waitqueue::getIsWaiting, 1)
                            .set(Waitqueue::getWaitingTime, BigDecimal.valueOf(0))
                            .set(Waitqueue::getLastRequestTime, new Date())
                            .set(Waitqueue::getWaitingTime, 0)
                            .update();
                }
            }
        }

        List<Waitqueue> waitqueueList = waitqueueService.lambdaQuery().list();
        if (airconditioner == null) {
            return null;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        Date lastStartTime = airconditioner.getLastStartTime();
        LocalDateTime lastAcUseTime = LocalDateTime.ofInstant(lastStartTime.toInstant(), ZoneId.systemDefault());
        Duration duration = Duration.between(lastAcUseTime, nowTime);
        //计算空调距离数据库中的lastUseTime到现在的时间差
        long seconds = duration.toSeconds();
        if (airconditioner.getPower() == 1) {
            //空调在使用过程中，实时计算费用
            //log.info("距离上次使用的秒数:{}", seconds);
            BigDecimal fee = BigDecimal.valueOf(1.0 * seconds * airconditioner.getSpeed() / 3 / 60);
            //room更新费用
            room.setAcFee(room.getAcFee().add(fee));
            room.setAcUsageTime(room.getAcUsageTime().add(BigDecimal.valueOf(1.0 * seconds / 3 / 60)));
            room.setTotalFee(room.getTotalFee().add(fee));
            if (room.getCurrentTemperature().compareTo(airconditioner.getTemperature()) < 0) {
                //房间温度小于设定温度
                room.setCurrentTemperature(room.getCurrentTemperature().add(BigDecimal.valueOf(1.0*RateEnums.getEnumByValue(rate).getText() * seconds / 60)));
            } else {
                //房间温度大于设定温度
                room.setCurrentTemperature(room.getCurrentTemperature().subtract(BigDecimal.valueOf(1.0 *RateEnums.getEnumByValue(rate).getText()* seconds / 60)));
            }
            roomService.lambdaUpdate().eq(Room::getRoomNumber, room_number).update(room);
            //airconditioner更新使用时间
            airconditioner.setLastStartTime(new Date());
            this.lambdaUpdate().eq(Airconditioner::getId, room_number)
                    .set(Airconditioner::getLastStartTime, new Date())
                    .update();
        } else {
            //空调未使用，更新房间温度
            if (room.getCurrentTemperature().compareTo(BigDecimal.valueOf(room.getEnvironmentTemperature())) < 0) {
                //房间温度小于环境温度
                roomService.lambdaUpdate().eq(Room::getRoomNumber, room_number)
                        .set(Room::getCurrentTemperature,
                                room.getCurrentTemperature().add(BigDecimal.valueOf( 0.5*seconds/ 60)));
            } else {
                //房间温度大于环境温度
                roomService.lambdaUpdate().eq(Room::getRoomNumber, room_number)
                        .set(Room::getCurrentTemperature,
                                room.getCurrentTemperature().subtract(BigDecimal.valueOf((0.5*seconds / 60))));
            }
        }
        StatusRsp result = new StatusRsp();
        result.setMode(airconditioner.getMode());
        result.setSpeed(SpeedEnums.getEnumByValue(airconditioner.getSpeed()).getText());
        result.setPower((airconditioner.getPower() == 1));
        result.setCost(room.getAcFee());
        //queue里面
        result.setQueue(String.valueOf(waitqueueThis.getIsWaiting()));
        result.setTime(room.getAcUsageTime());
        return result;
    }
}




