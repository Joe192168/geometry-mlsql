package com.geominfo.mlsql.controller.script;

import com.geominfo.authing.common.constants.ResourceTypeConstants;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.services.MlsqlService;
import com.geominfo.mlsql.services.ScriptService;
import com.geominfo.mlsql.utils.ExecuteShellUtil;
import com.geominfo.mlsql.utils.TreeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.font.Script;

import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @title: ScriptManagementController
 * @date 2021/6/1 15:03
 */
@RestController
@RequestMapping("/script")
@Slf4j
@Api(tags = {"脚本管理接口"})
public class ScriptManagementController {

    @Autowired
    private MlsqlService mlsqlService;

    @Autowired
    private ScriptService scriptService;

    /***
     * @Description: 执行shell脚本接口
     * @Author: zrd
     * @Date: 2021/5/12 10:57
     * @param
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "执行shell脚本接口",httpMethod = "GET")
    @GetMapping("/executeShell")
    public Message executeShellScript(String ip,Integer port,String username,String password,String shellCmd) {
        try {
            ExecuteShellUtil instance = ExecuteShellUtil.getInstance();
            instance.init(ip,port,username,password);
            instance.execCmd(shellCmd);
            return new Message().ok("重启成功");
        } catch (Exception e) {
            log.error("执行重启引擎脚本失败:{}",e.getMessage());
            return new Message().error("重启失败:"+e.getMessage());
        }
    }

    /***
     * @Description: 获取所有脚本，树状展示
     * @Author: zrd
     * @Date: 2021/5/13 15:33
     * @param
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "获取所有脚本，树状展示",httpMethod = "GET")
    @GetMapping("/script/{workSpaceId}")
    @ApiImplicitParam(name = "workSpaceId",value = "工作空间id",type = "BigDecimal",paramType = "path",required = true)
    public Message getAllScript(@PathVariable BigDecimal workSpaceId) {
        List<TreeVo<TSystemResources>> treeVos = mlsqlService.listTreeByParentId(new BigDecimal(ResourceTypeConstants.FOLDER),workSpaceId);
        return new Message().ok().addData("data",treeVos);
    }

    /***
     * @Description: 根据路径路径获取脚本
     * @Author: zrd
     * @Date: 2021/5/13 15:33
     * @param scriptRoute
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "根据路径路径获取脚本",httpMethod = "POST")
    @PostMapping("/script/getScriptByRoute")
    @ApiImplicitParam(name = "scriptRoute",value = "脚本路径",type = "String",paramType = "param",required = true)
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

    /***
     * @Description: 根据脚本id获取脚本
     * @Author: zrd
     * @Date: 2021/5/13 15:34
     * @param resourceId
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "根据脚本id获取脚本",httpMethod = "GET")
    @GetMapping("/script/getScriptById")
    @ApiImplicitParam(name = "resourceId",value = "脚本资源id",type = "BigDecimal",paramType = "param",required = true)
    public Message getScriptById(@RequestParam BigDecimal resourceId) {
        if (resourceId != null) {
            TSystemResources tSystemResources = mlsqlService.getScriptById(resourceId);
            if (tSystemResources != null) {
                return new Message().ok().addData("data",tSystemResources);
            }else {
                return new Message().error("获取脚本失败，请检查id是否存在");
            }
        }
        return new Message().error("脚本id为空");
    }

    /***
     * @Description: 根据脚本名获取脚本日志
     * @Author: zrd
     * @Date: 2021/5/13 15:37
     * @param jobName 脚本名
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "根据脚本名获取脚本日志",httpMethod = "GET")
    @GetMapping("/getJobLog/{jobName}")
    @ApiImplicitParam(name = "jobName",value = "脚本名",type = "String",paramType = "path",required = true)
    public Message getJobLogByJobName(@PathVariable String jobName) {
        Map<String, Object> jobLogByJobName = mlsqlService.getJobLogByJobName(jobName);
        if (jobLogByJobName != null) {
            return new Message().ok().addData("data",jobLogByJobName.get("msg"));
        }
        return new Message().error();
    }

    /***
     * @Description: 保存脚本接口
     * @Author: zrd
     * @Date: 2021/6/1 17:51
     * @param parentId
     * @param owner
     * @param content
     * @param scriptName
     * @param path
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "保存脚本接口",httpMethod = "POST")
    @PostMapping("/saveScript")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId",value = "父节点id",type = "BigDecimal",paramType = "param",required = true),
            @ApiImplicitParam(name = "owner",value = "脚本所有人",type = "BigDecimal",paramType = "param",required = true),
            @ApiImplicitParam(name = "content",value = "脚本内容",type = "String",paramType = "param",required = true),
            @ApiImplicitParam(name = "scriptName",value = "脚本名",type = "String",paramType = "param",required = true),
            @ApiImplicitParam(name = "path",value = "脚本路径",type = "String",paramType = "param",required = true)
    })
    public Message saveScript(@RequestParam BigDecimal parentId, @RequestParam BigDecimal owner,@RequestParam String content,
                              @RequestParam String scriptName,@RequestParam String path){
        boolean saveScript = mlsqlService.saveScript(parentId, owner, content, scriptName, path);
        if (saveScript) {
            return new Message().ok("保存成功");
        }
        return new Message().error("脚本已存在");
    }

    /***
     * @Description: 创建文件夹接口
     * @Author: zrd
     * @Date: 2021/6/3 14:36
     * @param parentId 文件夹父级
     * @param owner 文件夹拥有者
     * @return com.geominfo.mlsql.commons.Message
     */
    @PostMapping("/mkdir")
    @ApiOperation(value = "创建文件夹接口",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId",value = "父级资源id",type = "BigDecimal",paramType = "param",required = true),
            @ApiImplicitParam(name = "owner",value = "文件夹所有人",type = "String",paramType = "param",required = true),
            @ApiImplicitParam(name = "dirName",value = "文件夹名",type = "String",paramType = "param",required = true)
    })
    public Message mkdir(@RequestParam BigDecimal parentId, @RequestParam BigDecimal owner,@RequestParam String dirName){
        boolean mkdir = scriptService.mkdir(parentId,owner,dirName);
        if (mkdir) {
            return new Message().ok("创建成功");
        }
        return new Message().error("创建失败,文件夹已存在");
    }

    /***
     * @Description: 删除文件夹
     * @Author: zrd
     * @Date: 2021/6/3 14:36
     * @param resourceId 被删除文件夹资源id
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "删除文件夹",httpMethod = "DELETE")
    @DeleteMapping("/deleteDir/{resourceId}")
    @ApiImplicitParam(name = "resourceId",value = "资源id",type = "BigDecimal",paramType = "path",required = true)
    public Message deleteDir(@PathVariable BigDecimal resourceId){
        boolean mkdir = scriptService.deleteDir(resourceId);
        if (mkdir) {
            return new Message().ok("删除成功");
        }
        return new Message().error("删除失败,请先删除文件夹下资源");
    }

    /***
     * @Description: 修改文件夹名
     * @Author: zrd
     * @Date: 2021/6/3 14:36
     * @param resourceId 文件夹资源id
     * @param name 新修改文件夹名
     * @return com.geominfo.mlsql.commons.Message
     */
    @PutMapping("/modifyDir")
    @ApiOperation(value = "修改文件夹名",httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "文件名",type = "String",paramType = "param",required = true),
            @ApiImplicitParam(name = "resourceId",value = "文件夹资源id",type = "BigDecimal",paramType = "param",required = true),
            @ApiImplicitParam(name = "parentId",value = "父目录id",type = "BigDecimal",paramType = "param",required = true)
    })
    public Message deleteDir(@RequestParam String name,@RequestParam BigDecimal resourceId,@RequestParam BigDecimal parentId){
        boolean mkdir = scriptService.modifyDirName(name,resourceId,parentId);
        if (mkdir) {
            return new Message().ok("修改成功");
        }
        return new Message().error("修改失败,文件名已存在");
    }

    /***
     * @Description: 删除脚本
     * @Author: zrd
     * @Date: 2021/6/3 14:36
     * @param resourceId 文件夹资源id
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "删除脚本",httpMethod = "DELETE")
    @DeleteMapping("/deleteScript/{resourceId}")
    @ApiImplicitParam(name = "resourceId",value = "资源id",type = "BigDecimal",paramType = "path",required = true)
    public Message deleteScript(@PathVariable BigDecimal resourceId){
        boolean mkdir = scriptService.deleteScript(resourceId);
        if (mkdir) {
            return new Message().ok("删除成功");
        }
        return new Message().error("删除失败");
    }
}
