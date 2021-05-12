package com.geominfo.mlsql.controller;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.constant.ExceptionMsgConstant;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.vo.MlsqlExecuteSqlVO;
import com.geominfo.mlsql.services.MlsqlService;
import com.geominfo.mlsql.utils.ExecuteShellUtil;
import com.geominfo.mlsql.utils.TreeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
     * @Description: 执行shell脚本接口
     * @Author: zrd
     * @Date: 2021/5/12 10:57
     * @param
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/executeShell")
    public Message executeShellScript() {
        try {
            ExecuteShellUtil instance = ExecuteShellUtil.getInstance();
            instance.init("192.186.0.1",22,"root","root");
            instance.execCmd("test.sh");
            return new Message().ok("重启成功");
        } catch (Exception e) {
            log.error("执行重启引擎脚本失败:{}",e.getMessage());
            return new Message().error("重启失败:"+e.getMessage());
        }
    }

    @GetMapping("/script")
    public Message getAllScript() {
        List<TreeVo<TSystemResources>> treeVos = mlsqlService.listTreeByParentId(new BigDecimal(66));
        return new Message().ok().addData("data",treeVos);
    }

    @PostMapping("/script/getScriptByRoute")
    public Message getScriptByRoute(@RequestParam String scriptRoute) {
        if (StringUtils.isNotEmpty(scriptRoute)) {
            TSystemResources scriptByRoute = mlsqlService.getScriptByRoute(scriptRoute);
            if (scriptByRoute != null) {
                return new Message().ok().addData("data",scriptByRoute);
            }else {
                return new Message().error("获取脚本失败，请检查路径");
            }
        }
        return new Message().error("脚本路径为空");

    }

    @PostMapping("/script/getScriptById")
    public Message getScriptById(@RequestParam BigDecimal id) {
        if (id != null) {
            TSystemResources tSystemResources = mlsqlService.getScriptById(id);
            if (tSystemResources != null) {
                return new Message().ok().addData("data",tSystemResources);
            }else {
                return new Message().error("获取脚本失败，请检查id是否存在");
            }
        }
        return new Message().error("脚本id为空");

    }
}
