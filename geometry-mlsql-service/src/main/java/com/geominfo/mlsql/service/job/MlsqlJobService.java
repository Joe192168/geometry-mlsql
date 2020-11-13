package com.geominfo.mlsql.service.job;

import com.geominfo.mlsql.domain.vo.MlsqlJob;
import com.geominfo.mlsql.domain.vo.MlsqlJobRender;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 获取用户任务信息接口
 * @author: ryan
 * @create: 2020-11-10 09:25
 * @version: 3.0.0
 */
public interface MlsqlJobService {

    /**
     * description: 获取用户历史任务信息集合
     * author: ryan
     * date: 2020/11/09
     * param: 用户id
     * return: list集合
     */
    List<MlsqlJobRender> getMlsqlJobList(Integer userId);


    /**
     * description: 获取单个任务信息
     * author: ryan
     * date: 2020/11/09
     * param: userId：用户id， jobName: 任务名称
     * return: MlsqlJob
     */
    MlsqlJob getMlsqlJob(Integer userId, String jobName);


    /**
     * description: 修改任务信息
     * author: ryan
     * date: 2020/11/13
     * param: mlsqlJob
     * return: String
     */
    String updateMlsqlJob(Map<String, Object> map);


    /**
     * description: 创建map
     * author: ryan
     * date: 2020/11/13
     * param: mlsqlJob
     * return: Map
     */
    Map<String, Object> createMap(Integer userId, String jobName, Integer status,
                                  Long finshAt, String reason, String response);


}
