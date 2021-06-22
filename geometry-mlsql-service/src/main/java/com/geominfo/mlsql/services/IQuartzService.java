package com.geominfo.mlsql.services;

import com.geominfo.mlsql.domain.vo.JobParameter;
import org.springframework.scheduling.quartz.QuartzJobBean;
import java.util.List;
import java.util.Map;

public interface IQuartzService {

    void addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, int jobTime,int jobTimes);
    void addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, int jobTime, int jobTimes, JobParameter jobParam);
    void addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobTime);
    void addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobTime, JobParameter jobParam);
    void updateJob(String jobName, String jobGroupName, String jobTime);
    void updateJob(String jobName, String jobGroupName, String jobTime, JobParameter jobParam);
    void deleteJob(String jobName, String jobGroupName);
    void pauseJob(String jobName, String jobGroupName);
    void resumeJob(String jobName, String jobGroupName);
    void runAJobNow(String jobName, String jobGroupName);
    List<Map<String, Object>> queryAllJob();
    List<Map<String, Object>> queryRunJob();
    void updateScheduleJobWithParams(String jobName, String jobGroup, String cronExpression,String parm);

}
