package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @title: EngineDetailVO
 * @date 2021/6/2415:00
 */
@Data
public class EngineDetailsVO {

    private BigDecimal owner;

    private String groupName;

    private String monitorType;

    private String day;

    private Integer hour;

    private String engineUrl;

    private String port;

    private Long monitorIntervalTime;

    private int open;

    private int exceptionSolution;

    private String scriptRoute;

    private String username;

    private String password;

    public String getEngineUrl() {
        return engineUrl + ":" + this.port;
    }
}
