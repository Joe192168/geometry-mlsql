package com.geominfo.mlsql.controller.progress;

import com.alibaba.fastjson.JSONArray;
import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlProgressInfo;
import com.geominfo.mlsql.service.progress.ExecutedProgressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @program: geometry-mlsql
 * @description: 脚本执行进度监听控制类
 * @author: BJZ
 * @create: 2020-06-11 15:50
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/progress")
@Api(value="脚本执行监听进度口类",tags={"脚本执行监听进度口类"})
@Log4j2
public class ExecutedProgressController  extends BaseController{

    @Autowired
    private Message message ;

    @Autowired
    private ExecutedProgressService executedProgressService;

    /**
     * @description: 获取脚本执行进度回调接口
     *
     * @author: BJZ
     *
     * @date: 2020/6/11 0011
     *
     * @param:
     *
     * @return: 脚本执行进度
     */
    @RequestMapping("/getprogresscallback")
    @ApiOperation(value = "获取资源回调接口地址", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(value = "stat", name = "stat",
            dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "res", name = "res",
                    dataType = "String", paramType = "query", required = false)
    })
    public Message getprogresscallback(@RequestParam(value = "stat", required = false) String stat
            , @RequestParam(value = "res", required = false) String res) {

        log.info("获取执行进度回调接口得到数据=" + stat + "  res= " + res);

        List<MlsqlProgressInfo> jsonList = JSONArray.parseArray(res, MlsqlProgressInfo.class);
        if(CollectionUtils.isEmpty(jsonList))
        {
            return message.error(400, "JSON结果集为空，解析失败").addData("data", jsonList);
        }

        MlsqlProgressInfo mlsqlJobInfo  = jsonList.get(0) ;
        log.info(mlsqlJobInfo.getCompletedJobsNum());

        return message.ok(200, "get progress success").addData("data", mlsqlJobInfo);
    }


    /**
     * @description:  获取脚本执行进度数据
     * @author: BJZ
     * @date: 2020/6/11 0011
     * @param: 执行脚本名称，异步回调接口
     * @return:
     */
    @RequestMapping("/getprogress")
    @ApiOperation(value = "获取脚本执行进度接口", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(value = "jobName", name = "jobName",
            dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "callBackUrl", name = "callBackUrl",
                    dataType = "String", paramType = "query", required = true)
    })
    public Message getprogress(@RequestParam(value = "jobName", required = true) String jobName
            , @RequestParam(value = "callBackUrl", required = true) String callBackUrl) throws ExecutionException, InterruptedException {

        executedProgressService.getProgress(jobName, callBackUrl);
        return message.ok(200, "get progress success").addData("data", "success");

    }
}