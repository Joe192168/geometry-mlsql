package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @title: MlsqlJobsVO
 * @date 2021/4/710:19
 */
@Data
public class MlsqlJobsVO {

    private String owner;
    private String jobType;
    private String jobName;
    private String jobContent;
    private String groupId;
    private Long startTime;
    private Long timeout;
    private Progress progress;

    @Data
    public static class Progress {
        private int totalJob;
        private int currentJobIndex;
        private String script;
    }
}
