package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: MLSQL JOB执行信息
 * @author: BJZ
 * @create: 2020-06-10 14:54
 * @version: 1.0.0
 */
@Data
public class MlsqlJobInfo implements Serializable {

   private String groupId;
   private Integer activeJobsNum;
   private Integer completedJobsNum;
   private Integer failedJobsNum;
   private List<MsqlActiveJobs> msqlActiveJobsList;

}