package com.geominfo.mlsql.controller.engine;

import com.geominfo.mlsql.domain.vo.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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
                    @RequestParam(value="skipGrammarValidate", defaultValue="true") String skipGrammarValidate
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
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        ResponseEntity<String> responseEntityPost = restTemplate.postForEntity(engileUrl, requestEntity, String.class);
        if(responseEntityPost.getStatusCode().value() == 200 ){
            return message
                    .ok(200, "engine success")
                    .addData("data", responseEntityPost.getBody());
        }else{
            return message
                    .error(400, "engine  faild")
                    .addData("data", sql);
        }
    }
    @RequestMapping("/async/result")
    @ApiOperation(value = "异步执行返回接口", httpMethod = "POST")
    public Message getAsyncRunSqlResult(@RequestParam Map<String, String> map){
        if(map != null && map.get("stat").equals("succeeded"))
        {
            log.info("result status：" + map.get("stat"));
            log.info("result：" + map.get("res"));
            return message.ok("async get success");
        }
        return message.error("aysnc get faild");
    }
}
