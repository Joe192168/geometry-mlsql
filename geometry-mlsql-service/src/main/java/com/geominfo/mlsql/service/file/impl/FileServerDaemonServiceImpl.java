package com.geominfo.mlsql.service.file.impl;



import com.geominfo.mlsql.globalconstant.GlobalConstant;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: geometry-mlsql
 * @description:  定时清除旧的文件
 * @author: BJZ
 * @create: 2020-07-09 14:43
 * @version: 1.0.0
 */
@Service
public class FileServerDaemonServiceImpl {

    Logger logger = LoggerFactory.getLogger(FileServerDaemonServiceImpl.class);

    public void init(){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor() ;
        AtomicLong upLoadTime = new AtomicLong(GlobalConstant.ZERO) ;
        if(upLoadTime.getAndIncrement() == GlobalConstant.ZERO)
            run(executor) ;
    }

    private void run(ScheduledExecutorService executor)
    {
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                File file = new File(GlobalConstant.DEFAULT_TEMP_PATH) ;
                Arrays.stream(file.listFiles()).forEach(f -> {
                    if(System.currentTimeMillis() - f.lastModified() > GlobalConstant.FILE_MAX_BYTES)
                    {
                        try {
                            FileUtils.deleteDirectory(f);
                        } catch (IOException e) {
                            logger.error("Delete upload file fail", e) ;
                            e.printStackTrace();
                        }
                    }
                }) ;
            }
        } ,GlobalConstant.SIXTY , GlobalConstant.SIXTY , TimeUnit.SECONDS) ;
    }



}