package com.geominfo.mlsql.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



import java.util.Date;

/**
 * @title: TScriptExecLog
 * @date 2021/5/710:46
 */

@Data
public class TScriptExecLog {

    @TableId( type = IdType.AUTO)
    private Long id;

    private String jobId;

    private String jobName;

    private Integer jobType;

    private String scriptId;

    private String scriptName;

    private String scriptContent;

    private String execResult;

    private Integer execStatus;

    private String keyMsg;

    private Long operatorId;

    private Date operatorTime;

    private Date finishTime;

    public void setJobType(String jobType) {
        if (jobType.equals("script")) {
            this.jobType = 1;
        }else if (jobType.equals("stream")) {
            this.jobType = 2;
        }else
            this.jobType = 3;
    }

    public void setExecStatus(String exec_status) {
        if (exec_status.equals("succeeded")) {
            this.execStatus = 0;
        }else {
            this.execStatus = 1;
        }
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }
}
