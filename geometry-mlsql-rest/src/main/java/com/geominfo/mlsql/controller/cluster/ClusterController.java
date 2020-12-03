package com.geominfo.mlsql.controller.cluster;


import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.*;
import com.geominfo.mlsql.service.cluster.ApplyService;
import com.geominfo.mlsql.service.cluster.BackendService;
import com.geominfo.mlsql.service.cluster.ClusterService;
import com.geominfo.mlsql.service.cluster.DsService;
import com.geominfo.mlsql.service.engine.EngineService;
import com.geominfo.mlsql.service.job.MlsqlJobService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.utils.ParamsUtil;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.concurrent.ConcurrentRuntimeException;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.omg.CORBA.ServiceInformationHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @program: geometry-mlsql
 * @description: 集群后台配置控制类
 * @author:BJZ
 * @create: 2020-07-14 10:08
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/cluster")
@Api(value = "集群后台配置接口", tags = {"集群后台配置接口"})
@Log4j2
public class ClusterController extends BaseController {

    Logger logger = LoggerFactory.getLogger(ClusterController.class);

    @Autowired
    private ClusterService clusterService;

    @RequestMapping(value = "/api_v1/cluster" ,method = RequestMethod.POST)
    @ApiOperation(value = "集群后台配置接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "执行动作所有接口都要传的参数", name = "action", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "/backend/name/check接口需要传的参数", name = "name", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/add 接口需要传的参数", name = "teamName", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/list 接口需要传的参数", name = "tag", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/list/names 接口需要传的参数", name = "names", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/add 接口需要传的参数", name = "url", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/tags/update 接口需要传的参数", name = "id", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/tags/update 接口需要传的参数", name = "merge", dataType = "String", paramType = "query", required = false)

    })
    public Message clusterManager(@RequestBody ClusterManagerParameter clusterManagerParameter) throws Exception {

       Map<String, Object> params = ParamsUtil.objectToMap(clusterManagerParameter);
        ResponseEntity<String> result = clusterService.clusterManager(params);
        return result.getStatusCode().value() == 200 ?
                        success(200, "success").addData("data", result.getBody()) :
                        error(400, "error").addData("data", result.getBody());

    }



//    @RequestMapping(value = "/api_v1/run/script" ,method = RequestMethod.POST)
//    @ApiOperation(value = "执行脚本接口", httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "参数实体类", name = "sql",
//                    dataType = "String", paramType = "query", required = true)
//
//    })
//    public Message runScript(@RequestBody MLSQLRunScriptParameter mlsqlRunScriptParameter) throws Exception {
//
//        Map<String, Object> params = ParamsUtil.objectToMap(mlsqlRunScriptParameter);
//        if (MapUtils.isEmpty(params)) return error(400, "参数为空!");
//        if (!params.containsKey("owner")) params.put("owner", userName);
//        Map<Integer ,Object> resMap= clusterService.runScript(params);
//        logger.info("执行脚本返回结果 resMap = " + resMap);
//        int statudsCode = 0;
//        String res = "" ;
//        if(!resMap.isEmpty()){
//             statudsCode = resMap.keySet().iterator().next() ;
//            res = (String) resMap.values().iterator().next();
//        }
//
//        return statudsCode == 200 ?
//                success(statudsCode, "success").addData("data", res) :
//                error(statudsCode, "error").addData("data", res);
//
//    }

    @RequestMapping(value = "/api_v1/run/script" ,method = RequestMethod.POST)
    @ApiOperation(value = "执行脚本接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "MLSQL script content", name = "sql", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "the user who execute this API default: admin", name = "owner", dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "tscript|stream|sql; default is script", name = "jobType", dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "query|analyze; default is query", name = "executeMode", dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "the last sql in the script will return nothing. default: false", name = "silence", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "If set true, the owner will have their own session otherwise share the same. default: false", name = "sessionPerUser", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "If set true ,please also provide a callback url use `callback` parameter and the job will run in background and the API will return.  default: false", name = "async", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "Used when async is set true. callback is a url. default: false", name = "callback", dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "disable include statement. default: false", name = "skipInclude", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "disable table authorize . default: true", name = "skipAuth", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "validate mlsql grammar. default: true", name = "skipGrammarValidate", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "proxy parameter,filter backend with this tags", name = "tags", dataType = "String", paramType = "query")
    })
    public Message runScript(
            @RequestParam(value = "sql", required = true) String sql,
            @RequestParam(value="owner", defaultValue = "admin") String owner,
            @RequestParam(value="jobType", defaultValue = "script") String jobType,
            @RequestParam(value="executeMode", defaultValue = "query") String executeMode,
            @RequestParam(value="silence", defaultValue = "false") String silence,
            @RequestParam(value="sessionPerUser", defaultValue="false") String sessionPerUser,
            @RequestParam(value="async", defaultValue="true") String async,
            @RequestParam(value="callback", defaultValue="") String callback,
            @RequestParam(value="skipInclude", defaultValue="false") String skipInclude,
            @RequestParam(value="skipAuth", defaultValue="true") String skipAuth,
            @RequestParam(value="skipGrammarValidate", defaultValue="true") String skipGrammarValidate,
            @RequestParam(value="tags", defaultValue="") String tags
    )throws Exception {

        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("sql" ,sql ) ;
        params.put("owner" ,owner ) ;
        params.put("jobType" ,jobType ) ;
        params.put("executeMode" ,executeMode ) ;
        params.put("silence" ,silence ) ;
        params.put("sessionPerUser" ,sessionPerUser ) ;
        params.put("async" ,async ) ;
        params.put("callback" ,callback ) ;
        params.put("skipInclude" ,skipInclude ) ;
        params.put("skipAuth" ,skipAuth ) ;
        params.put("skipGrammarValidate" ,skipGrammarValidate ) ;
        params.put("skipGramtagsmarValidate" ,tags ) ;

        if (MapUtils.isEmpty(params)) return error(400, "参数为空!");
        if (!params.containsKey("owner")) params.put("owner", userName);
        ConcurrentHashMap<Integer ,Object> resMap= clusterService.runScript(params);
        logger.info("执行脚本返回结果 resMap = " + resMap);
        int statudsCode = 0;
        String res = "" ;
        if(!resMap.isEmpty()){
            statudsCode = resMap.keySet().iterator().next() ;
            res = (String) resMap.values().iterator().next();
        }

        return statudsCode == 200 ?
                success(statudsCode, "success").addData("data", res) :
                error(statudsCode, "error").addData("data", res);

    }




    @RequestMapping(value ="/api_v1/test001" ,method = RequestMethod.POST)
    @ApiOperation(value = "测试", httpMethod = "POST")
    public Message test001() throws Exception {

        Map<String, Object> params = new ConcurrentHashMap<>() ;
        params.put("sql" , SQL);
        params.put("owner" , "banjianzu@gmail.com");
        params.put("jobName" , UUID.randomUUID().toString());
//        params.put("queryType" , "robot");
        params.put("async" , "false");
//        params.put("engineName" , "undefined");
        params.put("callback" , "http://192.168.20.209:8088/api_v1/job/callback?__auth_secret__=mlsql");
        Map<Integer ,Object> resMap= clusterService.runScript(params);

        log.info("resMap = " + resMap);

        return null ;

    }



    private static final String SQL =" set user=\"root\";\n" +
            " set password=\"123456\";\n" +
            " \n" +
            " connect jdbc where\n" +
            " url=\"jdbc:mysql://192.168.2.239:3306/framework?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false\"\n" +
            " and driver=\"com.mysql.jdbc.Driver\"\n" +
            " and user=\"${user}\"\n" +
            " and password=\"${password}\"\n" +
            " as mydb_1;\n" +
            " \n" +
            " load jdbc.`mydb_1.schedule_job_log`  as  tmp ;\n" +
            " \n" +
            " select * from tmp as tmp ;" ;







}