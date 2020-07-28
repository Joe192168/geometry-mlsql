package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: geometry-mlsql
 * @description: 集群后台配置参数对象封装类
 * @author: BJZ
 * @create: 2020-07-28 17:17
 * @version: 1.0.0
 */
@Data
public class ClusterManagerParameter implements Serializable {

    private String action ;
    private String teamName ;
    private String name ;

}