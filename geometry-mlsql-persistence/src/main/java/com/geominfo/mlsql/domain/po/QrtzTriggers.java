package com.geominfo.mlsql.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @title: QrtzTriggers
 * @date 2021/6/2914:38
 */
@Data
@TableName("qrtz_triggers")
public class QrtzTriggers {

    private String schedName;

    private String triggerName;

    private String triggerGroup;

    private String jobName;

    private String jobGroup;

    private String description;

    private Long nextFireTime;

    private Long prevFireTime;

    private Integer priority;

    private String triggerState;

    private String triggerType;

    private Long startTime;

    private Long endTime;

    private String calendarName;

    private Long misfireInstr;

    private String jobData;
}
