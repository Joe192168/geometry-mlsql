package com.geominfo.mlsql.service.progress.impl;


import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.cluster.ClusterUrlService;
import com.geominfo.mlsql.service.progress.ExecutedProgressService;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.util.concurrent.ExecutionException;


/**
 * @program: geometry-mlsql
 * @description: 脚本执行进度资源获取服务接口
 * @author: BJZ
 * @create: 2020-06-10 17:48
 * @version: 1.0.0
 */
@Service
@Log4j2
public class ExecutedProgressServiceImpl implements ExecutedProgressService {


   @Autowired
   private ClusterUrlService clusterUrlService ;

    /**
      * @description: 异步获取脚本执行进度
      *
      * @author: BJZ
      *
      * @date: 2020/6/11 0011
      *
      * @param: jobName  ,callBackUtil 回调接口
      *
      * @return:
     */
    @Override
    public void getProgress( String jobName , String callBackUrl ) throws ExecutionException, InterruptedException {

        String script = GlobalConstant.SHOW_JOBS + jobName.trim() +  GlobalConstant.SEMICOLON;
        LinkedMultiValueMap<String, String>  curpostParameters = new LinkedMultiValueMap<>();
        curpostParameters.add(GlobalConstant.SQL ,script);
        curpostParameters.add( GlobalConstant.CALLBACK, callBackUrl.trim());
        clusterUrlService.aynRunScript(curpostParameters) ;
//        netWorkUtil.aynPost(GlobalConstant.RUN_SCRIPT ,curpostParameters);
    }




}