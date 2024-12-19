package com.haole.bupthotelbackend.service.impl;

import com.alibaba.excel.EasyExcel;
import com.haole.bupthotelbackend.model.ContractData;
import com.haole.bupthotelbackend.model.domain.Customer;
import com.haole.bupthotelbackend.model.domain.Room;
import com.haole.bupthotelbackend.service.CustomerService;
import com.haole.bupthotelbackend.service.RoomService;
import com.haole.bupthotelbackend.service.ExcelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Service
public class ExcelServiceImpl implements ExcelService {
    @Resource
    private RoomService roomService;

    @Resource
    private CustomerService customerService;

    private String PATH = "F:\\2024Java-Study\\bupt\\bupt_se_hotelSys\\bupt-hotel-backend\\src\\main\\resources\\excels\\";


    public List<ContractData> data(Room room, Customer customer) {
        List<ContractData> list = new ArrayList<>();
        ContractData data = new ContractData();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        data.setRoom_number(room.getRoomNumber());
        data.setCheckin_date(dateFormat.format(room.getCheckInDate()));
        data.setCheckout_date(dateFormat.format(room.getCheckOutDate()));
        data.setAcFee(room.getAcFee());
        data.setTotalFee(room.getTotalFee());
        data.setId_Card(customer.getIdCard());
        data.setName(customer.getName());
        list.add(data);
        return list;
    }

    public void writeContract(Integer room_number) {
        Room room = roomService.getById(room_number);
        Customer customer = customerService.lambdaQuery().eq(Customer::getRoomNumberId, room_number).one();

        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
        String room_no = String.valueOf(room_number);
        // 写法1 JDK8+
        // since: 3.0.0-beta1
        String fileName = PATH + "room" + room_no + ".xlsx";

        EasyExcel.write(fileName, ContractData.class).sheet("模板").doWrite(data(room, customer));
    }


}
