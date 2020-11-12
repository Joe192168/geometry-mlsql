package com.geominfo.mlsql.service.job;

import com.geominfo.mlsql.domain.vo.MlsqlJob;
import com.geominfo.mlsql.domain.vo.MlsqlJobRender;

import java.util.List;

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
    List<MlsqlJob> getMlsqlJobList(Integer userId);


    /**
     * description: 获取单个任务信息
     * author: ryan
     * date: 2020/11/09
     * param: userId：用户id， jobName: 任务名称
     * return: MlsqlJob
     */
    MlsqlJob getMlsqlJob(Integer userId, String jobName);


    /**
     * description: 渲染输出
     * author: ryan
     * date: 2020/11/11
     * param: mlsqlJob
     * return: MlsqlJobRender
     */
    MlsqlJobRender mlsqlJobReaner(MlsqlJob mlsqlJob);


    /**
     * description: 杀死进程
     * author: ryan
     * date: 2020/11/11
     * param: userId, mlsqlJob
     * return: int
     */
    String updateMlsqlJobStatus(Integer userId, MlsqlJob mlsqlJob);


    /**
     * description: 执行成功需要修改信息
     * author: ryan
     * date: 2020/11/12
     * param: response, jobName, status, finishAt
     * return: int
     */
    String updateStatusAndResponse(MlsqlJob mlsqlJob);
    /*int updateStatusAndResponse(String jobName, Integer status,
                                String response, Long finishAt);*/

    /**
     * description: 执行失败需要修改信息
     * author: ryan
     * date: 2020/11/12
     * param: reason, jobName, status, finishAt
     * return: int
     */
    String updateStatusAndReason(MlsqlJob mlsqlJob);
    /*int updateStatusAndReason(String jobName,Integer status,
                              String reason, Long finishAt);*/


}
