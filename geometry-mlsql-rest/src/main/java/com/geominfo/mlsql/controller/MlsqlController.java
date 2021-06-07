package com.geominfo.mlsql.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.constant.ExceptionMsgConstant;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TEtFunctionInfo;
import com.geominfo.mlsql.domain.po.TScriptExecLog;
import com.geominfo.mlsql.domain.po.TScriptExecMetricLog;
import com.geominfo.mlsql.domain.vo.MlsqlExecuteSqlVO;
import com.geominfo.mlsql.services.MlsqlService;
import com.geominfo.mlsql.services.NumberControlService;
import com.geominfo.mlsql.utils.TreeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @title: MlsqlController
 * @date 2021/4/6 14:40
 */

@RestController
@RequestMapping("/mlsql")
@Slf4j
public class MlsqlController {

    @Autowired
    private MlsqlService mlsqlService;

    @Autowired
    private TSystemResourcesDao tSystemResourcesDao;

    @Autowired
    private NumberControlService numberControlService;
    /***
     * @Description: 执行mlsql sql接口
     * @Author: zrd
     * @Date: 2021/5/10 10:19
     * @param mlsqlExecuteSqlVO
     * @return com.geominfo.mlsql.commons.Message
     */
    @PostMapping("/executeSql")
    public Message executeSql(@RequestBody( required = false) MlsqlExecuteSqlVO mlsqlExecuteSqlVO){
        try {
            String result = mlsqlService.executeMlsql(mlsqlExecuteSqlVO);
            return new Message().ok().addData("data",result);
        } catch (Exception e) {
            log.error("executeSql : {}",e.getMessage());
            return new Message().error(ExceptionMsgConstant.MLSQL_NOT_RESPONSE);
        }
    }

    /***
     * @Description: 获取正在执行的所有任务
     * @Author: zrd
     * @Date: 2021/5/10 10:19
     * @param
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/getAlljobs")
    public Message getRunningJobs(){
        try {
            JSONObject allExecuteJobs = mlsqlService.getAllExecuteJobs();
            return new Message().ok().addData("data",allExecuteJobs);
        } catch (Exception e) {
            log.error("getRunningJobs : {}",e.getMessage());
            return new Message().error(ExceptionMsgConstant.MLSQL_NOT_RESPONSE);
        }
    }

    /***
     * @Description: 获取引擎的状态
     * @Author: zrd
     * @Date: 2021/5/10 10:20
     * @param
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/getEngineState")
    public Message getEngineState(){
        try {
            JSONObject allExecuteJobs = mlsqlService.getEngineState();
            return new Message().ok().addData("data",allExecuteJobs);
        } catch (Exception e) {
            log.error("getEngineState : {}",e.getMessage());
            return new Message().error(ExceptionMsgConstant.MLSQL_NOT_RESPONSE);
        }
    }

    /***
     * @Description: 杀死任务
     * @Author: zrd
     * @Date: 2021/5/10 10:20
     * @param jobName 任务名
     * @param groupId id
     * @return com.geominfo.mlsql.commons.Message
     */
    @PostMapping("/killJob")
    public Message killJob(@RequestParam(required = false) String jobName,@RequestParam(required = false) String groupId){
        try {
            String result = mlsqlService.killMlsqlJob(jobName, groupId);
            return new Message().ok().addData("data",result);
        } catch (Exception e) {
            log.error("getEngineState : {}",e.getMessage());
            return new Message().error(ExceptionMsgConstant.MLSQL_NOT_RESPONSE);
        }
    }

    /***
     * @Description: 异步回调接口
     * @Author: zrd
     * @Date: 2021/5/6 14:16
     * @param
     * @param
     * @return com.geominfo.mlsql.commons.Message
     */
    @PostMapping("/asyncCallback")
    public void asyncCallback(@RequestParam Map<String,String> map){
        mlsqlService.dealAsyncCallback(map);
    }



    /***
     * @Description: 检查作业是否完成
     * @Author: zrd
     * @Date: 2021/5/14 14:40
     * @param jobName 任务名
     * @return
     */
    @GetMapping("/checkJobIsFinish/{jobName}")
    public Message checkJobIsFinish(@PathVariable String jobName) {
        JSONObject jsonObject = mlsqlService.checkJobIsFinish(jobName);
        return new Message().ok().addData("data",jsonObject);
    }

    /***
     * @Description: 语法检查接口
     * @Author: zrd
     * @Date: 2021/5/18 9:34
     * @param sql
     * @param owner
     * @param jobType
     * @param executeMode
     * @param jobName
     * @param sessionPerUser
     * @param skipInclude
     * @param skipGrammarValidate
     * @param contextDefaultIncludeFetchUrl
     * @return com.geominfo.mlsql.commons.Message
     */
    @PostMapping("/grammarCheck")
    public Message GrammarCheck(@RequestParam String sql,@RequestParam String owner,@RequestParam String jobType,@RequestParam String executeMode,
                                @RequestParam String jobName,@RequestParam Boolean sessionPerUser,@RequestParam Boolean skipInclude,
                                @RequestParam Boolean skipGrammarValidate,@RequestParam String contextDefaultIncludeFetchUrl){
        try {
            JSONArray jsonObject =  mlsqlService.grammarCheck(sql,owner,jobType,executeMode,jobName,sessionPerUser,skipInclude,skipGrammarValidate,contextDefaultIncludeFetchUrl);
            return new Message().ok().addData("data", jsonObject);
        }catch (Exception e) {
            return new Message().error().addData("msg",e.getMessage());
        }
    }


    /***
     * @Description: 获取所有函数，树状展示
     * @Author: zrd
     * @Date: 2021/5/13 15:33
     * @param
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/function")
    public Message getFunction() {
        List<TreeVo<TEtFunctionInfo>> treeVos = mlsqlService.listFunctionTreeByParentId(new BigDecimal(2));
        return new Message().ok().addData("data",treeVos);
    }

    /***
     * @Description: 获取所有插件，树状展示
     * @Author: zrd
     * @Date: 2021/5/13 15:33
     * @param
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/plugin")
    public Message getPlugin() {
        List<TreeVo<TEtFunctionInfo>> treeVos = mlsqlService.listFunctionTreeByParentId(new BigDecimal(1));
        return new Message().ok().addData("data",treeVos);
    }

    /***
     * @Description: 获取任务执行进度接口
     * @Author: zrd
     * @Date: 2021/5/21 11:19
     * @param sql
     * @param owner
     * @param jobType
     * @param executeMode
     * @param jobName
     * @param sessionPerUser
     * @return com.geominfo.mlsql.commons.Message
     */
    @PostMapping("/getScriptExecuteProgress")
    public Message getScriptExecuteProgress(@RequestParam(required = false) String sql,@RequestParam(required = false) String owner,@RequestParam(required = false) String jobType,
                                            @RequestParam(required = false) String executeMode,@RequestParam(required = false) String jobName,@RequestParam(required = false) Boolean sessionPerUser){
        String result = mlsqlService.getScriptExecuteProgress(sql, owner, jobType, executeMode, jobName, sessionPerUser);
        return new Message().ok().addData("data",result);
    }

    /***
     * @Description: 获取异步执行任务结果
     * @Author: zrd
     * @Date: 2021/5/27 14:26
     * @param jobId 任务名
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/getAsyncJobResult/{jobId}")
    public Message getAsyncLogResult(@PathVariable String jobId) {
       TScriptExecLog tScriptExecLog =  mlsqlService.getAsyncLogResult(jobId);
       if (tScriptExecLog != null) {
           return new Message().ok().addData("data",tScriptExecLog);
       }
       return new Message().error("任务不存在");
    }

    /***
     * @Description: 获取执行历史-运行时详情
     * @Author: zrd
     * @Date: 2021/5/28 13:51
     * @param jobId
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/getRuntimeDetails/{jobId}")
    public Message getRuntimeDeatils(@PathVariable String jobId) {
        TScriptExecMetricLog runtimeDetails = mlsqlService.getRuntimeDetails(jobId);
        if (runtimeDetails != null) {
            return new Message().ok().addData("data",runtimeDetails);
        }
        return new Message().error("任务不存在");
    }


    /***
     * @Description: 删除任务历史记录
     * @Author: zrd
     * @Date: 2021/6/1 9:55
     * @param jobId 任务id
     * @return com.geominfo.mlsql.commons.Message
     */
    @DeleteMapping("/deleteJobHistory/{jobId}")
    public Message deleteJobHistory(@PathVariable String jobId) {
        Boolean flag = mlsqlService.deleteJobHistory(jobId);
        if (flag) {
            return new Message().ok("删除成功");
        }
        return new Message().error("任务不存在");
    }


}
