package com.haole.bupthotelbackend.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.haole.bupthotelbackend.model.ContractData;
import com.haole.bupthotelbackend.model.domain.Customer;
import com.haole.bupthotelbackend.model.domain.Record;
import com.haole.bupthotelbackend.model.domain.Room;
import com.haole.bupthotelbackend.service.CustomerService;
import com.haole.bupthotelbackend.service.RoomService;
import com.haole.bupthotelbackend.service.ExcelService;
import com.haole.bupthotelbackend.service.GlobalRecordStorage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {
    @Resource
    private RoomService roomService;

    @Resource
    private CustomerService customerService;

    private String PATH = "C:\\Users\\meinfurher\\bupt-hotel-backend1\\src\\main\\resources\\excels\\";

    public List<ContractData> data(Room room, Customer customer) {
        List<ContractData> list = new ArrayList<>();
        ContractData data = new ContractData();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        data.setRoom_number(room.getRoomNumber());
        data.setCheckin_date(room.getCheckInDate() != null ? dateFormat.format(room.getCheckInDate()) : "N/A");
        data.setCheckout_date(room.getCheckOutDate() != null ? dateFormat.format(room.getCheckOutDate()) : "N/A");
        data.setAcFee(room.getAcFee());
        data.setTotalFee(room.getTotalFee());

        if (customer != null) {
            data.setId_Card(customer.getIdCard());
            data.setName(customer.getName());
        } else {
            data.setId_Card("N/A");
            data.setName("N/A");
        }

        list.add(data);
        return list;
    }

    public void writeContract(Integer room_number) {
        Room room = roomService.getById(room_number);
        Customer customer = customerService.lambdaQuery()
                .eq(Customer::getRoomNumberId, room_number)
                .eq(Customer::getIsIn, 1)
                .one();

        // 添加日志以检查 customer 对象是否正确查询到
        if (customer == null) {
            log.error("Customer not found for room number: {}", room_number);
        } else {
            log.info("Customer found for room number: {}", room_number);
        }

        // 获取对应房间的记录
        List<Record> records = GlobalRecordStorage.getInstance().getRecords(Long.valueOf(room_number));

        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
        String room_no = String.valueOf(room_number);
        // 写法1 JDK8+
        // since: 3.0.0-beta1
        String fileName = PATH + "room" + room_no + ".xlsx";

        // 使用 ExcelWriter 对象来管理多个 sheet 的写入
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();

        // 写入合同数据到 sheet1
        WriteSheet writeSheet1 = EasyExcel.writerSheet("合同").head(ContractData.class).build();
        excelWriter.write(data(room, customer), writeSheet1);

        // 写入记录数据到 sheet2
        WriteSheet writeSheet2 = EasyExcel.writerSheet("记录").head(Record.class).build();
        excelWriter.write(records, writeSheet2);

        // 关闭 ExcelWriter 对象
        excelWriter.finish();
    }
}
