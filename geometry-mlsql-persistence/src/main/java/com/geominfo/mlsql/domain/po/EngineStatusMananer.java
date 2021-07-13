package com.geominfo.mlsql.domain.po;

import lombok.Data;
import org.apache.tomcat.util.digester.ObjectCreateRule;

import java.util.Date;

/**
 * @title: EngineStatusMananer
 * @date 2021/7/217:25
 */
@Data
public class EngineStatusMananer {

    private Long id;

    private String engineName;

    private String engineUrl;

    private Long interval;

    private Integer monitorStatus;

    private Integer dealType;

    private Date monitorTime;
}
