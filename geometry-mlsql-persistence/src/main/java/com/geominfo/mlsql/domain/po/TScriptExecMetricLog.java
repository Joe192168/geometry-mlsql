package com.geominfo.mlsql.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @title: TScriptExecMetricLog
 * @date 2021/5/815:25
 */
@Data
public class TScriptExecMetricLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String jobId;

    private Integer sparkUiJobCnt;

    private Integer sparkUiStageCnt;

    private Integer sparkUiTaskCnt;

    private String explainMsg;

    private String extraOpts;

    private Date createTime;

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
