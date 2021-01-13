package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: geometry-mlsql
 * @description: ScriptRun
 * @author: BJZ
 * @create: 2021-01-11 15:32
 * @version: 1.0.0
 */
@Data
public class ScriptRun {

    private String sql  = "select 1 as a,'jack' as b as bbc;";
    private String owner = "admin";
    private String jobType = "script";
    private String executeMode = "query";
    private String silence = "false";
    private String sessionPerUser = "false";
    private String async = "true";
    private String callback ;
    private String skipInclude = "false" ;
    private String skipAuth = "true" ;
    private String skipGrammarValidate = "true" ;
    private String tags  ;
}