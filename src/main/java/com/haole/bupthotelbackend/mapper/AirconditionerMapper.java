package com.haole.bupthotelbackend.mapper;

import com.haole.bupthotelbackend.model.domain.Airconditioner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author liu haole
* @description 针对表【airconditioner】的数据库操作Mapper
* @createDate 2024-12-23 15:12:35
* @Entity com.haole.bupthotelbackend.model.domain.Airconditioner
*/
public interface AirconditionerMapper extends BaseMapper<Airconditioner> {
    @Select("SELECT * FROM airconditioner WHERE roomId = #{roomNumber} LIMIT 1")
    Airconditioner selectByRoomNumber(String roomNumber);
}




