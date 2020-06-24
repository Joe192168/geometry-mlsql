package com.geominfo.mlsql.controller.engine;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.config.restful.CustomException;
import com.geominfo.mlsql.domain.vo.MLSQLJobInfo;
import com.geominfo.mlsql.domain.vo.Message;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: EngineConntroller
 * @author: anan
 * @create: 2020-06-04 10:45
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/run")
@Api(value="Engine执行接口类",tags={"Engine执行sql接口"})
@Log4j2
public class EngineConntroller {
    @Autowired
    private Message message ;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${engine.url}")
    private String engileUrl;

    @RequestMapping("/script")
    @ApiOperation(value = "执行sql接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "MLSQL script content",name = "sql",dataType = "String",paramType = "query",required = true),
            @ApiImplicitParam(value = "the user who execute this API default: admin",name = "owner",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "tscript|stream|sql; default is script",name = "jobType",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "query|analyze; default is query",name = "executeMode",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "the last sql in the script will return nothing. default: false",name = "silence",dataType = "boolean",paramType = "query"),
            @ApiImplicitParam(value = "If set true, the owner will have their own session otherwise share the same. default: false",name = "sessionPerUser",dataType = "boolean",paramType = "query"),
            @ApiImplicitParam(value = "If set true ,please also provide a callback url use `callback` parameter and the job will run in background and the API will return.  default: false",name = "async",dataType = "boolean",paramType = "query"),
            @ApiImplicitParam(value = "Used when async is set true. callback is a url. default: false",name = "callback",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "disable include statement. default: false",name = "skipInclude",dataType = "boolean",paramType = "query"),
            @ApiImplicitParam(value = "disable table authorize . default: true",name = "skipAuth",dataType = "boolean",paramType = "query"),
            @ApiImplicitParam(value = "validate mlsql grammar. default: true",name = "skipGrammarValidate",dataType = "boolean",paramType = "query"),
            @ApiImplicitParam(value = "validate auth client",name = "context.__auth_client__",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "validate auth url. default: true",name = "context.__auth_server_url__",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "validate auth secret. default: true",name = "context.__auth_secret__",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "proxy parameter,filter backend with this tags",name = "tags",dataType = "String",paramType = "query")
    })
    public Message run(@RequestParam(value = "sql", required = true) String sql,
                    @RequestParam(value="owner", defaultValue = "admin") String owner,
                    @RequestParam(value="jobType", defaultValue = "script") String jobType,
                    @RequestParam(value="executeMode", defaultValue = "query") String executeMode,
                    @RequestParam(value="silence", defaultValue = "false") String silence,
                    @RequestParam(value="sessionPerUser", defaultValue="false") String sessionPerUser,
                    @RequestParam(value="async", defaultValue="false") String async,
                    @RequestParam(value="callback", defaultValue="false") String callback,
                    @RequestParam(value="skipInclude", defaultValue="false") String skipInclude,
                    @RequestParam(value="skipAuth", defaultValue="true") String skipAuth,
                    @RequestParam(value="skipGrammarValidate", defaultValue="true") String skipGrammarValidate,
                    @RequestParam(value="context.__auth_client__", defaultValue="") String auth_client,
                    @RequestParam(value="context.__auth_server_url__", defaultValue="") String auth_server_url,
                    @RequestParam(value="context.__auth_secret__", defaultValue="") String auth_secret,
                    @RequestParam(value="tags", defaultValue="") String tags
                     ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
        postParameters.add("sql", sql);
        postParameters.add("owner", owner);
        postParameters.add("jobType", jobType);
        postParameters.add("executeMode", executeMode);
        postParameters.add("silence", silence);
        postParameters.add("sessionPerUser", sessionPerUser);
        postParameters.add("async", async);
        postParameters.add("callback", callback);
        postParameters.add("skipInclude", skipInclude);
        postParameters.add("skipAuth", skipAuth);
        postParameters.add("skipGrammarValidate", skipGrammarValidate);
        postParameters.add("context.__auth_client__", auth_client);
        postParameters.add("context.__auth_server_url__", auth_server_url);
        postParameters.add("context.__auth_secret__", auth_secret);
        postParameters.add("tags", tags);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        ResponseEntity<String> responseEntityPost = null;
        try {
            responseEntityPost = restTemplate.postForEntity(engileUrl, requestEntity, String.class);
            return message
                    .ok(200, "engine send success")
                    .addData("data", responseEntityPost.getBody());
        }catch (CustomException e){
            log.info("getBody_error : {}", e.getBody());
            return message
                    .error(400, "engine send faild")
                    .addData("data", e.getBody());
        }
    }
    @RequestMapping("/async/result")
    @ApiOperation(value = "异步执行返回接口", httpMethod = "POST")
    public Message getAsyncRunSqlResult(@RequestParam Map<String, String> map){
        if(map.size()>0 )
        {
            String stat = map.get("stat").toString();
            String result = stat.equals("succeeded") == true? map.get("res").toString() : map.get("msg").toString();
            MLSQLJobInfo mlsqlJobInfo = JSONObject.parseObject(map.get("jobInfo")).toJavaObject(MLSQLJobInfo.class);
            log.info("async message status ：{}" , stat);
            log.info("async message content ： {}", result);
            log.info("async message jobInfo groupId：{}, jobName: {}, owner: {}", mlsqlJobInfo.getGroupId()
                    ,mlsqlJobInfo.getJobName(), mlsqlJobInfo.getOwner()
            );
            return message.ok("async get message sucess");
        }
        return message.error("aysnc get faild");
    }

}
