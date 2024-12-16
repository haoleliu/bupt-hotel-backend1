package com.haole.bupthotelbackend.RoomTest;


import com.haole.bupthotelbackend.model.domain.Room;
import com.haole.bupthotelbackend.service.RoomService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoomSelectTest {

    @Resource
    private RoomService roomService;


    @Test
    public void selectTest() {
        Room one = roomService.lambdaQuery().eq(Room::getRoomNumber, 1).one();
        System.out.println(one);


    }

}
