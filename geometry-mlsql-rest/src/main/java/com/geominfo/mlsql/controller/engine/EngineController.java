package com.geominfo.mlsql.controller.engine;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.config.restful.CustomException;
import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.*;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.service.cluster.BackendService;
import com.geominfo.mlsql.service.engine.EngineService;
import com.geominfo.mlsql.service.user.TeamRoleService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import com.geominfo.mlsql.util.ExtractClassMsgUtil;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: EngineConntroller
 * @author: anan
 * @create: 2020-06-04 10:45
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/api_v1")
@Api(value="Engine维护接口类",tags={"Engine维护接口"})
@Log4j2
public class EngineController extends BaseController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamRoleService teamRoleService;

    @Autowired
    private BackendService backendService;

    @Autowired
    private EngineService engineService;

    @Value("${engine.url}")
    private String engileUrl;

    @RequestMapping(value = "/engine/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增engine", httpMethod = "POST")
//    public Message addEngine(@RequestParam MlsqlEngine mlsqlEngine){
    public Message addEngine(){
        MlsqlEngine mlsqlEngine = new MlsqlEngine();
        mlsqlEngine.setName("awh_test2");
        mlsqlEngine.setUrl("10.0.0.151:9003");
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        if(mlsqlUser.getRole().equals("admin") == false){
            return success(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.ONLY_ADMIN_OPRATOR).addData("data", mlsqlUser.getRole());
        }

        if(mlsqlEngine.getName() == null
                || mlsqlEngine.getName().equals("") == true
                || mlsqlEngine.getUrl() == null
                || mlsqlEngine.getUrl().equals("") == true
                ){
            return success(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.ENGINE_NAME_USER_REQUEST);
        }
        List<MlsqlEngine> mlsqlEngineByName = engineService.findByName(mlsqlEngine.getName());
        if(mlsqlEngineByName.size()>0){
            mlsqlEngine.setId(mlsqlEngineByName.get(0).getId());
            engineService.updateEngine(mlsqlEngine);
        }else{
            engineService.insertEngine(mlsqlEngine);
        }
        return success(ReturnCode.RETURN_SUCCESS_STATUS, InterfaceReturnInformation.SAVE_SUCCESS).addData("data", "");
    }

    @RequestMapping(value = "/engine/register", method = RequestMethod.POST)
    @ApiOperation(value = "注册engine", httpMethod = "POST")
//    public Message registerEngine(@RequestParam MlsqlEngine mlsqlEngine){
    public Message registerEngine(){
        MlsqlEngine mlsqlEngine = new MlsqlEngine();
        mlsqlEngine.setName("awh_test1");
        mlsqlEngine.setUrl("10.0.0.150:9003");
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        if(mlsqlEngine.getName() == null
                || mlsqlEngine.getName().equals("") == true
                || mlsqlEngine.getUrl() == null
                || mlsqlEngine.getUrl().equals("") == true
                ){
            return success(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.ENGINE_NAME_USER_REQUEST);
        }
        List<MlsqlEngine> mlsqlEngineByName = engineService.findByName(mlsqlEngine.getName());
        if(mlsqlEngineByName.size()>0){
            mlsqlEngine.setId(mlsqlEngineByName.get(0).getId());
            engineService.updateEngine(mlsqlEngine);
        }else{
            engineService.insertEngine(mlsqlEngine);
            mlsqlEngineByName = engineService.findByName(mlsqlEngine.getName());
            //是否存在default组
            List<MlsqlGroup> mlsqlGroupList = teamRoleService.getTeam(mlsqlUser.getId(), MlsqlGroupUser.owner);
            Boolean flag = false;
            for(MlsqlGroup mlsqlGroup : mlsqlGroupList){
                if(mlsqlGroup.getName().equals("default")){flag = true;}
            }
            if(flag){
                backendService.intsertBackendProxy("default", mlsqlEngineByName.get(0).getName());
            }else{
                teamRoleService.createTeam(mlsqlUser,"default");
                backendService.intsertBackendProxy("default", mlsqlEngineByName.get(0).getName());
            }
        }
        return success(ReturnCode.RETURN_SUCCESS_STATUS, InterfaceReturnInformation.REGISTER_USER_SUCESS);
    }

    @RequestMapping(value = "/engine/list", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "engine列表")
    public Message engineList(){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        List<MlsqlEngine> mlsqlEngineList = null;
        if(mlsqlUser.getRole().equals("admin")){
            mlsqlEngineList = engineService.findByName("");
        }else{
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("status", MlsqlGroupUser.confirmed + "," + MlsqlGroupUser.owner);
            paraMap.put("userId", mlsqlUser.getId());
            mlsqlEngineList = engineService.getAllEngine(paraMap);
        }
        return success(ReturnCode.RETURN_SUCCESS_STATUS, InterfaceReturnInformation.QUERY_SUCCESS)
                .addData("schema", ExtractClassMsgUtil.extractClassName(MlsqlEngine.class))
                .addData("data", mlsqlEngineList);
    }

    @RequestMapping(value = "/engine/current", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "engine列表")
    public Message current(@RequestParam Map<String, String> map){
        return success(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.USER_NOT_EXISTS).addData("data", "");
    }

    @RequestMapping(value = "/engine/remove", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "删除engine")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "engineId",name = "id",dataType = "int",paramType = "query",required = true)
    })
    public Message remove(@RequestParam(value = "id", required = true) int id){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        if(mlsqlUser.getRole().equals("admin")){
            engineService.deleteEngineById(id);
        }
        return success(ReturnCode.RETURN_SUCCESS_STATUS, InterfaceReturnInformation.DELETE_SUCCESS)
                .addData("schema" , ExtractClassMsgUtil.extractClassName(MlsqlEngine.class))
                .addData("data", engineService.findByName(""));
    }

    @RequestMapping("/run/script")
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
            return success(200, "engine send success")
                    .addData("data", responseEntityPost.getBody());
        }catch (CustomException e){
            log.info("getBody_error : {}", e.getBody());
            return error(400, "engine send faild")
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
            return success(200, "async get message sucess");
        }
        return error(400,"aysnc get faild");
    }
}
