package com.haole.bupthotelbackend.service;

import com.haole.bupthotelbackend.model.StatusRsp;
import com.haole.bupthotelbackend.model.domain.Airconditioner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haole.bupthotelbackend.request.UpdateRequest;


/**
* @author liu haole
* @description 针对表【airconditioner】的数据库操作Service
* @createDate 2024-12-15 14:57:28
*/
public interface AirconditionerService extends IService<Airconditioner> {

    public Boolean updateAirconditioner(Long room_number,UpdateRequest request);

    public Airconditioner getByRoomNumber(String roomNumber);
    public StatusRsp getAirconditioner(Long room_number);

}
