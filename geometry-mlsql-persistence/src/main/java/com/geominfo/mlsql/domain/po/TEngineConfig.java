package com.geominfo.mlsql.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.geominfo.mlsql.commons.PageResult;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @title: TEngineConfig
 * @date 2021/7/110:13
 */
@Data
public class TEngineConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long engineId;

    private Long interval;

    private Long dealType;

    private Long operatorId;

    private Date createTime;

    private Date updateTime;

    private Long isEnable;

    private String extraOpts;
}
