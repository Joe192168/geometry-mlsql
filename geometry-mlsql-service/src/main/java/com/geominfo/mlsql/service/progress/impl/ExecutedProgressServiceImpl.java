package com.geominfo.mlsql.service.progress.impl;

import com.geominfo.mlsql.service.progress.ExecutedProgressService;
import com.geominfo.mlsql.utils.NetWorkUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;


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
    private NetWorkUtil netWorkUtil ;

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
    public void getProgress(String jobName ,String callBackUrl ) {

        if(StringUtils.isEmpty(jobName) || StringUtils.isEmpty(callBackUrl)){
            log.info("JobName 和 回调接口不能为空!");
           return ;
        }

        String script = "!show jobs  " + jobName.trim() + " ; ";
        MultiValueMap<String, String> curpostParameters = new LinkedMultiValueMap<String, String>();
        curpostParameters.add("sql" ,script);
        curpostParameters.add("callback", callBackUrl.trim());
        netWorkUtil.aynPost(curpostParameters);
    }


}