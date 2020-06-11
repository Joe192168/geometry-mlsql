package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MLSQLJobInfo
 * @author: anan
 * @create: 2020-06-11 17:51
 * @version: 1.0.0
 */
@Data
public class MLSQLJobInfo {
    private String owner;
    private String jobType;
    private String jobName;
    private String jobContent;
    private String groupId;
    private MLSQLJobProgress progress;
    private Long startTime;
    private Long timeout;
}
