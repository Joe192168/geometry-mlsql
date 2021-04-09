package com.geominfo.mlsql.services;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.domain.VO.MlsqlExecuteSqlVO;

/**
 * @Auther zrd
 * @Date 2021-04-06 10:13 
 *
 */
public interface MlsqlService {

    /***
     * @Description: 执行mlsql脚本方法
     * @Author: zrd
     * @Date: 9:40
     * @Param [mlsqlExecuteSqlVO]
     * @return void
     */
    String executeMlsql(MlsqlExecuteSqlVO mlsqlExecuteSqlVO);
    
    /***
     * @Description: 获取正在执行MLSQL 作业
     * @Author: zrd 
     * @Date: 9:42
     * @Param []
     * @return java.lang.String
     */
    JSONObject getAllExecuteJobs();

    /***
     * @Description: 杀掉正在执行的job任务
     * @Author: zrd
     * @Date: 9:42
     * @Param []
     * @return java.lang.String
     */
    String killMlsqlJob(String jobName,String groupId);

    /***
     * @Description: 获取engine状态
     * @Author: zrd
     * @Date: 11:39
     * @Param []
     * @return com.alibaba.fastjson.JSONObject
     */
    JSONObject getEngineState();
}
