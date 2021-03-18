package com.geominfo.mlsql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GeometryMlsqlRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeometryMlsqlRestApplication.class, args);
	}

}
