package com.haole.bupthotelbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
        Airconditioner trueAirconditioner = this.lambdaQuery().eq(Airconditioner::getId, room_number).one();
        List<Waitqueue> waitqueueList = waitqueueService.lambdaQuery().list();
        Boolean result=true;
        List<Airconditioner> airconditionersList=this.list();
        int inUsingNum=0;
        Waitqueue waitqueueThis=waitqueueService.lambdaQuery().eq(Waitqueue::getRoomNum, room_number).one();
        //找waitqueue is waiting
        for (Airconditioner airconditioner:airconditionersList){
            if (airconditioner.getPower()==1){
                inUsingNum++;
            }
        }
        //如果使用队列满了，加入等待队列中
        if (inUsingNum==2&&request.getPower()==true){
            result=false;
            request.setPower(false);
            waitqueueService.lambdaUpdate()
                    .eq(Waitqueue::getRoomNum, room_number)
                    .set(Waitqueue::getLastRequestTime,new Date())
                    .set(Waitqueue::getIsWaiting, 1)
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
        return result;
    }

    @Override
    public StatusRsp getAirconditioner(Long room_number) {
        int totalUsingNum=0;
        Airconditioner airconditioner = this.lambdaQuery().eq(Airconditioner::getId, room_number).one();
        Room room = roomService.lambdaQuery().eq(Room::getRoomNumber, room_number).one();
        List<Airconditioner> airconditionerList = this.lambdaQuery().list();
        //如果使用空调数小于2，则可以将等待队列中的空调启动
        for (Airconditioner airconditioner1 : airconditionerList) {
            if (airconditioner1.getPower() == 1) {
                totalUsingNum++;
            }
        }
        List<Waitqueue> list = waitqueueService.lambdaQuery().eq(Waitqueue::getIsWaiting, 1)
                .orderBy(true, false, Waitqueue::getLastRequestTime)
                .list();
        int size=list.size();//等待中的空调数量
        if (totalUsingNum < 2&&size>0) {
            int canUseNum = 2 - totalUsingNum;
            log.info("等待队列中的空调(按照上次调用时间降序):{}", list);
            for (int i = 0; i < Math.min(canUseNum,size); i++) {
                int num=list.get(i).getRoomNum();
                this.lambdaUpdate().eq(Airconditioner::getId, num).set(Airconditioner::getPower,1).update();
                waitqueueService.lambdaUpdate().eq(Waitqueue::getRoomNum, num).set(Waitqueue::getIsWaiting, 0).update();
            }
        }
        List<Waitqueue> waitqueueList = waitqueueService.lambdaQuery().list();
        Waitqueue waitqueueThis=waitqueueService.lambdaQuery().eq(Waitqueue::getRoomNum, room_number).one();
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
        //queue里面
        result.setQueue(String.valueOf(waitqueueThis.getIsWaiting()));
        result.setTime(room.getAcUsageTime());
        return result;
    }
}




