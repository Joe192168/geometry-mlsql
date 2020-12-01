package com.geominfo.mlsql.domain.vo;

import lombok.Data;


/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlJob
 * @author: ryan(丁帅波)
 * @create: 2020-11-09 17:43
 * @version: 1.0.0
 */
@Data
public class MlsqlJob {
    private Integer id;
    private String name;
    private String content;
    private Integer status;
    private Long createdAt;
    private Long finishAt;
    private String reason;
    private String response;
    private int mlsqlUserId;



    public static final  int RUNNING = 1 ;
    public static final  int SUCCESS = 2 ;
    public static final  int FAIL = 3 ;
    public static final  int RKILLED = 4 ;


    public MlsqlJob(Integer id,
                    String name,
                    String content,
                    Integer status,
                    int mlsqlUserId ,
                    Long createdAt,
                    Long finishAt,
                    String reason,
                    String response) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.finishAt = finishAt;
        this.reason = reason;
        this.response = response;
        this.mlsqlUserId = mlsqlUserId;
    }
}
