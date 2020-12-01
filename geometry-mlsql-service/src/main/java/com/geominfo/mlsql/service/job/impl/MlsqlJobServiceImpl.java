package com.geominfo.mlsql.service.job.impl;

import com.geominfo.mlsql.domain.vo.MlsqlJob;
import com.geominfo.mlsql.domain.vo.MlsqlJobRender;
import com.geominfo.mlsql.mapper.JobMapper;
import com.geominfo.mlsql.service.job.MlsqlJobService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 获取任务信息实现
 * @author: ryan
 * @create: 2020-11-10 09:27
 * @version: 1.0.0
 */
@Service
public class MlsqlJobServiceImpl implements MlsqlJobService {

    @Autowired
    JobMapper mlsqlJobMapper;

    public static final Integer RUNNING = 1;
    public static final Integer SUCCESS = 2;
    public static final Integer FAIL = 3;
    public static final Integer KILLED = 4;


    @Override
    public List<MlsqlJobRender> getMlsqlJobList(Integer userId) {
        return mlsqlJobMapper.getMlsqlJobList(userId);
    }

    @Override
    public MlsqlJob getMlsqlJob(Map<String,Object> map) {
        return mlsqlJobMapper.getMlsqlJob(map);
    }


    @Override
    public String updateMlsqlJob(Map<String, Object> map) {
        int result = mlsqlJobMapper.updateMlsqlJob(map);
        return result > 0? InterfaceReturnInformation.SUCCESS: InterfaceReturnInformation.FAILED;
    }

    @Override
    public Map<String, Object> createMap(Integer userId, String jobName,Integer status,
                                         Long finshAt,String reason,String response) {
        HashMap<String, Object> newMap = new HashMap<>();
        if (userId != 0){
            newMap.put("userid",userId);
        }
        if (!reason.isEmpty() && !reason.equals(" ")){
            newMap.put("reason",reason);
        }
        if (!reason.isEmpty() && !response.equals(" ")){
            newMap.put("response",response);
        }
        newMap.put("jobName",jobName);
        newMap.put("status",status);
        newMap.put("finishAt",finshAt);
        return newMap;
    }

    @Override
    public void insertJob(MlsqlJob mlsqlJob) {
        mlsqlJobMapper.insertJob(mlsqlJob);
    }


    @Override
    public String updateMlsqlJobByJonName(Map<String, Object> map){
        int result = mlsqlJobMapper.updateMlsqlJobByJonName(map);
        return result > 0? InterfaceReturnInformation.SUCCESS: InterfaceReturnInformation.FAILED;
    }

}
