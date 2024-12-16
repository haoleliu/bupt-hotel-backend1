package com.haole.bupthotelbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@MapperScan("com.haole.bupthotelbackend.mapper")
@SpringBootApplication
public class BuptHotelBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuptHotelBackendApplication.class, args);
	}

}
