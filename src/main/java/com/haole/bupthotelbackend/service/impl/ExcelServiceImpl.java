package com.haole.bupthotelbackend.service.impl;

import com.alibaba.excel.EasyExcel;
import com.haole.bupthotelbackend.model.ContractData;
import com.haole.bupthotelbackend.model.ServiceData;
import com.haole.bupthotelbackend.model.GlobalServiceData;
import com.haole.bupthotelbackend.model.domain.Airconditioner;
import com.haole.bupthotelbackend.model.domain.Customer;
import com.haole.bupthotelbackend.model.domain.Room;
import com.haole.bupthotelbackend.service.AirconditionerService;
import com.haole.bupthotelbackend.service.CustomerService;
import com.haole.bupthotelbackend.service.RoomService;
import com.haole.bupthotelbackend.service.ExcelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;


@Service
public class ExcelServiceImpl implements ExcelService {
    @Resource
    private RoomService roomService;

    @Resource
    private CustomerService customerService;

    @Resource
    private AirconditionerService airconditionerService;

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

    public Map<String, List<Object>> serviceData(Airconditioner airconditioner) {
        Map<String, List<Object>> roomData = new HashMap<>();
        roomData.put("请求时间", new ArrayList<>());
        roomData.put("服务开始时间", new ArrayList<>());
        roomData.put("服务结束时间", new ArrayList<>());
        roomData.put("服务时长", new ArrayList<>());
        roomData.put("风速", new ArrayList<>());
        roomData.put("当前费用", new ArrayList<>());
        roomData.put("费率", new ArrayList<>());

        roomData.get("请求时间").add(new Date()); // 当前时间作为请求时间
        roomData.get("服务开始时间").add(airconditioner.getLastStartTime());
        roomData.get("服务结束时间").add(new Date()); // 假设服务结束时间为当前时间
        roomData.get("服务时长").add(airconditioner.getAcUsageTime().toString());
        roomData.get("风速").add(airconditioner.getSpeed().toString());
        roomData.get("当前费用").add(new BigDecimal("100.00")); // 假设当前费用为缺省值
        roomData.get("费率").add(new BigDecimal("1.00")); // 假设费率为缺省值

        return roomData;
    }

    public void writeContract(Integer room_number) {
        Room room = roomService.getById(room_number);
        Customer customer = customerService.lambdaQuery()
                .eq(Customer::getRoomNumberId, room_number)
                .eq(Customer::getIsIn,1)
                .one();

        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
        String room_no = String.valueOf(room_number);
        // 写法1 JDK8+
        // since: 3.0.0-beta1
        String fileName = PATH + "room" + room_no + ".xlsx";

        EasyExcel.write(fileName, ContractData.class).sheet("模板").doWrite(data(room, customer));
        EasyExcel.write(fileName, ServiceData.class).sheet("服务数据").doWrite(convertToServiceDataList(GlobalServiceData.getServiceData(room.getRoomNumber())));
    }

    private List<ServiceData> convertToServiceDataList(Map<String, List<Object>> roomData) {
        List<ServiceData> serviceDataList = new ArrayList<>();
        for (int i = 0; i < roomData.get("请求时间").size(); i++) {
            ServiceData data = new ServiceData();
            data.setRequestTime((Date) roomData.get("请求时间").get(i));
            data.setServiceStartTime((Date) roomData.get("服务开始时间").get(i));
            data.setServiceEndTime((Date) roomData.get("服务结束时间").get(i));
            data.setServiceDuration((String) roomData.get("服务时长").get(i));
            data.setWindSpeed((String) roomData.get("风速").get(i));
            data.setCurrentFee((BigDecimal) roomData.get("当前费用").get(i));
            data.setRate((BigDecimal) roomData.get("费率").get(i));
            serviceDataList.add(data);
        }
        return serviceDataList;
    }

    public void loadServiceDataFromDatabase(Integer roomNumber) {
        Airconditioner airconditioner = airconditionerService.getByRoomNumber("房间" + roomNumber);
        if (airconditioner != null) {
            Map<String, List<Object>> serviceDataMap = serviceData(airconditioner);
            GlobalServiceData.addServiceData("房间" + roomNumber, serviceDataMap);
        }
    }
}

