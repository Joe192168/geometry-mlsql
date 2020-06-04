package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlUser
 * @author: anan
 * @create: 2020-06-03 12:00
 * @version: 1.0.0
 */
@Data
public class MlsqlUser implements Serializable{
    public static String STATUS_LOCK = "lock";
    private int id;
    private String name;
    private String password;
    private String backend_tags;
    private String role;
    private String status;
}
