package com.geominfo.mlsql.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @title: RestTemplateConfig
 * @date 2021/4/6 14:57
 */
@Configuration
public class RestTemplateConfig {

    @Bean("RestTemplateBean")
    public RestTemplate restTemplateConfig(){
        return new RestTemplate();
    }
}
