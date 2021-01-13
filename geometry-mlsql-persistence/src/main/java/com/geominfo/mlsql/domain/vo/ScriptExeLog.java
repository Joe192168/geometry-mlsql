package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import javax.annotation.security.DenyAll;
import java.io.Serializable;

/**
 * @program: geometry-mlsql
 * @description: ScriptExeLog
 * @author: BJZ
 * @create: 2021-01-06 15:09
 * @version: 1.0.0
 */
@Data
public class ScriptExeLog implements Serializable {

      private static final long serialVersionUID = 1L;

      private Integer id ;
      private Integer duration ;
      private String jobId ;

      private Integer job;
      private Integer stages;
      private Integer tasks;
      private Integer inPutSum;
      private String  inPutByte;
      private Integer  outPutSum;
      private String outPutBtye;
      private String logicalExecutionPlan ;
      private String physicalExecutionPlan ;
      private String remarks ;
      private String groupId ;



}