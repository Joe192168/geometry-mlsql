package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import javax.annotation.security.DenyAll;
import java.io.Serializable;
import java.util.Date;

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
      private String jobId ;
      private Integer sparkUiJobCnt ;
      private Integer sparkUiStageCnt ;
      private Integer sparkUiTaskCnt ;
      private String explainMsg ;
      private String extraOpts ;
      private Date createTime ;




}