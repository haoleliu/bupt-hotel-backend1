package com.haole.bupthotelbackend.CustomerTest;


import com.haole.bupthotelbackend.mapper.CustomerMapper;
import com.haole.bupthotelbackend.model.domain.Customer;
import com.haole.bupthotelbackend.service.CustomerService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SelectTest {

    @Resource
    private CustomerService customerService;
    @Test
    public void testSelect(){
        customerService.lambdaQuery()
                .eq(Customer::getRoomNumberId, 1)
                .list()
                .forEach(System.out::println);
    }
}
