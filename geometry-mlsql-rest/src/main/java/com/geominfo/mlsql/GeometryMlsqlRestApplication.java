package com.geominfo.mlsql;

import com.geominfo.authing.common.utils.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.geominfo.mlsql.dao")
public class GeometryMlsqlRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeometryMlsqlRestApplication.class, args);
	}

	/**
	 * 注入雪花算法产生唯一标识
	 * @return
	 */
	@Bean
	public IdWorker idWorker() {
		return new IdWorker();
	}

}
