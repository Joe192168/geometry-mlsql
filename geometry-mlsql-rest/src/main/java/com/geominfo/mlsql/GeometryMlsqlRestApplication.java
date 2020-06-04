package com.geominfo.mlsql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@ServletComponentScan
public class GeometryMlsqlRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeometryMlsqlRestApplication.class, args);
	}

}
