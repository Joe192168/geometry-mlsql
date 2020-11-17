package com.geominfo.mlsql.domain.vo;

import lombok.Data;
import springfox.documentation.service.Tags;

import java.io.Serializable;
import java.util.UUID;

/**
 * @program: geometry-mlsql
 * @description: 脚本参数对象封装类
 * @author: bjz
 * @create: 2020-07-27 16:15
 * @version: 1.0.0
 */
@Data
public class MLSQLRunScriptParameter implements Serializable {

    private String sql = "select 1 as a,'jack' as b as bbc;" ;
    private String jobName = UUID.randomUUID().toString();
    private String jobType ="script" ;
    private String callback ;
    private String skipAuth = "true";
    private String skipInclude ;
    private String silence ="false";
    private String sessionPerUser ="false";
    private String sessionPerRequest;
    private String async ="false";
    private String skipGrammarValidate ="true";
    private String timeout ="-1";
    private String executeMode ="query";
    private String tags ;
}