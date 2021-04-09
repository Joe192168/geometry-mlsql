package com.geominfo.mlsql.domain.VO;

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

/*    @Data
    public static class progress {
        private int totalJob;
        private int currentJobIndex;
        private String script;
    }*/
}
