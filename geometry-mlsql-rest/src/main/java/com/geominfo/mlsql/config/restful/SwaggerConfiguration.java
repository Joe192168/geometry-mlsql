package com.geominfo.mlsql.config.restful;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @program: geometry-bi
 * @description: swagger2配置，默认地址http://localhost:8088/swagger-ui.html
 * @author: 肖乔辉
 * @create: 2018-08-17 15:39
 * @version: 1.0.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.geominfo.mlsql.controller"))
				.paths(PathSelectors.any()).build().directModelSubstitute(Timestamp.class, Date.class);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("MLSQL CONSOLE 后端功能平台接口规范")
				.description("一站式大数据平台")
				.termsOfServiceUrl("http://localhost:8080/swagger-ui.html")
				.version("1.0")
				.build();
	}


}
