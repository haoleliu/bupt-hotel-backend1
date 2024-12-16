package com.haole.bupthotelbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haole.bupthotelbackend.model.domain.Customer;
import com.haole.bupthotelbackend.service.CustomerService;
import com.haole.bupthotelbackend.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

/**
* @author liu haole
* @description 针对表【customer】的数据库操作Service实现
* @createDate 2024-12-15 14:57:28
*/
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements CustomerService{

}




