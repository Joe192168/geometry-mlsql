package com.geominfo.mlsql.service.progress;


/**
 * @program: geometry-mlsql
 * @description: 脚本执行进度资源获取服务接口
 * @author: BJZ
 * @create: 2020-06-10 17:48
 * @version: 1.0.0
 */
public interface ExecutedProgressService {

    void getProgress(String jobName, String callBackUrl);

}