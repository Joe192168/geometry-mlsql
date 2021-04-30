package com.geominfo.mlsql.config;


import com.geominfo.mlsql.services.impl.HdfsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: HdfsConfig
 * @date 2021/4/26 9:49
 */
@Configuration
public class HdfsConfig {

    @Value("${hdfs.hdfsServer}")
    private String defaultHdfsUri;

    @Bean
    public HdfsService getHbaseService(){
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("fs.defaultFS",defaultHdfsUri);
        return new HdfsService(conf,defaultHdfsUri);
    }
}
