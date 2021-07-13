package com.geominfo.mlsql.domain.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @title: EngineDetailVO
 * @date 2021/6/2415:00
 */
@Data
public class EngineDetailsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long configId;

    private Long userId;

    private Long engineId;

    private String cron;

    private BigDecimal owner;

    private String groupName;

    //cron表达式类型
    private String monitorType;

    private String day;

    private Integer hour;

    private String engineUrl;

    private String port;

    private Long monitorIntervalTime;

    // 1表示开启，0表示不开启
    private Long open;

    //异常解决方案  1:重启  2:不重启
    private Long exceptionSolution;

    public String getEngineUrl() {
        return engineUrl + ":" + this.port;
    }

    private RestartConfig restartConfig;

    @Data
    public static class RestartConfig implements Serializable{
        private static final long serialVersionUID = 1L;

        private String username;

        private String password;

        private String scriptRoute;
    }
}
