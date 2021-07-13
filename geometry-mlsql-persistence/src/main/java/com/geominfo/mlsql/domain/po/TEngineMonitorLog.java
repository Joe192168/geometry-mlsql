package com.geominfo.mlsql.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @title: TEngineMonitorLog
 * @date 2021/7/111:45
 */
@Data
public class TEngineMonitorLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long configId;

    private Integer monitorStatus;

    private Date monitorTime;
}
