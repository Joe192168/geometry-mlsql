package com.geominfo.mlsql.controller.scriptfile;

import com.alibaba.fastjson.JSONArray;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlJobInfo;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.service.scriptfile.ExecutedProgressService;
import com.geominfo.mlsql.service.scriptfile.ScripteFileSvervice;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: UserScriptFileController
 * @author: BJZ
 * @create: 2020-06-04 15:59
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/scriptfile")
@Api(value = "MLSQL脚本类接口", tags = {"MLSQL脚本类"})
@Log4j2
public class UserScriptFileController {

    @Autowired
    private Message message;

    @Autowired
    private ScripteFileSvervice scripteFileSvervice;

    @Autowired
    private ExecutedProgressService executedProgressService;


    /**
      * @description: 通过ID获取脚本内容
      *
      * @author: BJZ
      *
      * @date: 2020/6/11 0011
      *
      * @param: 脚本ID
      *
      * @return:  脚本内容
     */

    @RequestMapping("/getcontent")
    @ApiOperation(value = "通过ID获取脚本内容", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(value = "脚本id", name = "id", dataType = "String", paramType = "query", required = true)
    })
    public Message getcontent(@RequestParam(value = "id", required = true) String id) {
        MlsqlScriptFile mlsqlScriptFile = scripteFileSvervice.getScriptById(Integer.valueOf(id));
        if (mlsqlScriptFile != null) {
            if (mlsqlScriptFile.getIs_dir() == 1) {
                return message.ok(200, "id is catalog").addData("data", mlsqlScriptFile);
            }
            return message.ok(200, "get content success").addData("data", mlsqlScriptFile);
        } else {
            return message.error(400, "id not exists").addData("data", id);
        }
    }


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

        List<MlsqlJobInfo> jsonList = JSONArray.parseArray(res, MlsqlJobInfo.class);
        if(jsonList.size() == 0 )
        {
            return message.error(400, "JSON结果集为空，解析失败").addData("data", jsonList);
        }

        MlsqlJobInfo mlsqlJobInfo  = jsonList.get(0) ;
        log.info(mlsqlJobInfo.getCompletedJobsNum());

        return message.ok(200, "get progress success").addData("data", mlsqlJobInfo);
    }


    /**
      * @description:  获取脚本执行进度数据
      *
      * @author: BJZ
      *
      * @date: 2020/6/11 0011
      *
      * @param: 执行脚本名称，异步回调接口
      *
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
            , @RequestParam(value = "callBackUrl", required = true) String callBackUrl) {


        if(StringUtils.isEmpty(jobName) || StringUtil.isEmpty(callBackUrl)){
            log.info("jobName 和 callback不能为空");
            return message.error(400, "JjobName 和 callback不能为空").
                    addData("data", "fail");
        }

        executedProgressService.getProgress(jobName, callBackUrl);
        return message.ok(200, "get progress success").addData("data", "success");

    }










}
