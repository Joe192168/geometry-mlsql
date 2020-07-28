package com.geominfo.mlsql.domain.vo;

import lombok.Data;
import springfox.documentation.service.Tags;

import java.io.Serializable;

/**
 * @program: geometry-mlsql
 * @description: 脚本参数对象封装类
 * @author: bjz
 * @create: 2020-07-27 16:15
 * @version: 1.0.0
 */
@Data
public class MLSQLRunScriptParameter implements Serializable {

    private String sql ;
    private String jobName ;
    private String jobType ;
    private String callback ;
    private String skipAuth;
    private String skipInclude;
    private String silence;
    private String sessionPerUser;
    private String sessionPerRequest;
    private String async;
    private String skipGrammarValidate;
    private String timeout;
    private String executeMode ;
    private String tags ;
}