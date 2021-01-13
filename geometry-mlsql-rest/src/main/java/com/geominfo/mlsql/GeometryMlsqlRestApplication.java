package com.geominfo.mlsql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@SpringBootApplication(exclude = {MultipartAutoConfiguration.class}) //关闭mutilpartfile自动配置 fileuplaod组件才起作用
//@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@ServletComponentScan
public class GeometryMlsqlRestApplication {


	public static void main(String[] args) {
		SpringApplication.run(GeometryMlsqlRestApplication.class, args);
	}

}
