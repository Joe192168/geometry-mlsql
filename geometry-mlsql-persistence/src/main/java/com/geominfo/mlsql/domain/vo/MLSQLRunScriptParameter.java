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
    private String owner = UUID.randomUUID().toString();
    private String jobType ="script" ;
    private String callback ;
    private Boolean skipAuth = true;
    private Boolean skipInclude = false ;
    private Boolean silence =false;
    private Boolean sessionPerUser =false;
    private Boolean sessionPerRequest = false;
    private Boolean async =true;
    private Boolean skipGrammarValidate =true;
    private Integer timeout =-1;
    private String executeMode ="query";
    private String tags="" ;
}