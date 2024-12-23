package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haole.bupthotelbackend.model.domain.Airconditioner;
import generator.service.AirconditionerService;
import com.haole.bupthotelbackend.mapper.AirconditionerMapper;
import org.springframework.stereotype.Service;

/**
* @author liu haole
* @description 针对表【airconditioner】的数据库操作Service实现
* @createDate 2024-12-23 15:12:35
*/
@Service
public class AirconditionerServiceImpl extends ServiceImpl<AirconditionerMapper, Airconditioner>
    implements AirconditionerService{

}




