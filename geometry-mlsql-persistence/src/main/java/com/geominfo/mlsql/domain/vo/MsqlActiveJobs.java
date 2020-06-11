package com.geominfo.mlsql.domain.vo;

import lombok.Data;
import scala.Int;

import java.io.Serializable;

/**
 * @program: geometry-mlsql
 * @description: MLSQL activeJobs 信息
 * @author: BJZ
 * @create: 2020-06-10 14:58
 * @version: 1.0.0
 */
@Data
public class MsqlActiveJobs  implements Serializable{

    private Integer jobId;
    private String submissionTime;
    private Integer numTasks;
    private Integer numActiveTasks;
    private Integer numCompletedTasks;
    private Integer numSkippedTasks;
    private Integer numFailedTasks;
    private Integer numKilledTasks;
    private Integer numCompletedIndices;
    private Integer numActiveStages;
    private Integer numCompletedStages;
    private Integer numSkippedStages;
    private Integer numFailedStages;
}