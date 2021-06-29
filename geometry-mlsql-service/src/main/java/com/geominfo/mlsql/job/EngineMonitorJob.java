package com.geominfo.mlsql.job;

import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @title: EngineMonitorJob
 * @date 2021/6/2511:31
 */
public class EngineMonitorJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("job执行了+"+ simpleDateFormat.format(new Date()));
    }
}
