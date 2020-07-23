package com.geominfo.mlsql.support.manager;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: geometry-bi
 * @description: 日志操作任务运行管理器
 * @author: 肖乔辉
 * @create: 2019-05-23 19:02
 * @version: 1.0.0
 */
public class LogExeManager {

    private static final int LOG_DELAY_TIME = 10;

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(20);

    private static LogExeManager logExeManager = new LogExeManager();
    private LogExeManager() {

    }

    public static LogExeManager getInstance() {
        return logExeManager;
    }

    public void executeLogTask(TimerTask timerTask) {
        executor.schedule(timerTask, LOG_DELAY_TIME, TimeUnit.MICROSECONDS);
    }
}
