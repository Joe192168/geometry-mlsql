package com.geominfo.mlsql.service.job.impl;

import com.geominfo.mlsql.domain.vo.MlsqlJob;
import com.geominfo.mlsql.domain.vo.MlsqlJobRender;
import com.geominfo.mlsql.mapper.MlsqlJobMapper;
import com.geominfo.mlsql.service.job.MlsqlJobService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import com.geominfo.mlsql.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    MlsqlJobMapper mlsqlJobMapper;

    public static final Integer RUNNING = 1;
    public static final Integer SUCCESS = 2;
    public static final Integer FAIL = 3;
    public static final Integer KILLED = 4;


    @Override
    public List<MlsqlJob> getMlsqlJobList(Integer userId) {
        return mlsqlJobMapper.getMlsqlJobList(userId);
    }

    @Override
    public MlsqlJob getMlsqlJob(Integer userId, String jobName) {
        return mlsqlJobMapper.getMlsqlJob(userId, jobName);
    }


    @Override
    public MlsqlJobRender mlsqlJobReaner(MlsqlJob mlsqlJob) {
        //获取脚本内容
        String content = mlsqlJob.getContent();
        String subContent;
        if (content.length() > 100) {
            //获取前100脚本内容
            subContent = content.substring(0, 100);
        } else {
            subContent = content.substring(0, content.length());
        }

        //获取状态
        String statusStr;
        switch (mlsqlJob.getStatus()) {
            case 1:
                statusStr = "running";
                break;
            case 2:
                statusStr = "success";
                break;
            case 3:
                statusStr = "fail";
                break;
            case 4:
                statusStr = "killed";
                break;
            default:
                statusStr = "";
        }

        //获取原因
        String reason = mlsqlJob.getReason();
        //截取前100原因内容
        String subReason;
        if (reason.length() > 100) {
            subReason = reason.substring(0, 100);
        } else {
            subReason = reason.substring(0, reason.length());
        }

        //获取创建时间
        String createAtDate = CommonUtil.dateTime(mlsqlJob.getCreatedAt());
        //获取结束时间
        String finishAtDate = CommonUtil.dateTime(mlsqlJob.getFinishAt());

        String response = mlsqlJob.getResponse();
        String subResponse;
        if (response.length() > 100) {
            subResponse = response.substring(0, 100);
            System.out.println(subResponse);
        } else {
            subResponse = response.substring(0, response.length());
        }

        return new MlsqlJobRender(mlsqlJob.getId(), mlsqlJob.getName(), subContent, statusStr, subReason, createAtDate, finishAtDate, subResponse);
    }

    @Override
    public String updateMlsqlJobStatus(Integer userId, MlsqlJob mlsqlJob) {
        int result = mlsqlJobMapper.updateMlsqlJobStatus(userId, mlsqlJob.getName(),mlsqlJob.getStatus(),mlsqlJob.getFinishAt());
        return result > 0 ? InterfaceReturnInformation.SUCCESS: InterfaceReturnInformation.FAILED;
    }

    @Override
    public String updateStatusAndResponse(MlsqlJob mlsqlJob) {
        int result = mlsqlJobMapper.updateStatusAndResponse(mlsqlJob.getName(),mlsqlJob.getStatus(),mlsqlJob.getResponse(),mlsqlJob.getFinishAt());
        return result > 0 ? InterfaceReturnInformation.SUCCESS: InterfaceReturnInformation.FAILED;
    }

    @Override
    public String updateStatusAndReason(MlsqlJob mlsqlJob) {
        int result = mlsqlJobMapper.updateStatusAndResponse(mlsqlJob.getName(),mlsqlJob.getStatus(),mlsqlJob.getReason(),mlsqlJob.getFinishAt());
        return result > 0? InterfaceReturnInformation.SUCCESS: InterfaceReturnInformation.FAILED;
    }


}
