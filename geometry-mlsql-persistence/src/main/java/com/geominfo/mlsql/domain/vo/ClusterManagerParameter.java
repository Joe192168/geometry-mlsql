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

    private String action ; //所有接口都必须传的参数
    private String teamName ;  // /backend/add 接口需要传的参数
    private String name ; //  /backend/name/check 接口需要传的参数
    private String tag; //  /backend/list 接口需要传的参数
    private String names; //  /backend/list/names 接口需要传的参数

}