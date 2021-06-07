package com.geominfo.mlsql.controller;

import com.geominfo.authing.common.constants.ResourceTypeConstants;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.services.MlsqlService;
import com.geominfo.mlsql.services.ScriptService;
import com.geominfo.mlsql.utils.ExecuteShellUtil;
import com.geominfo.mlsql.utils.TreeVo;
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

    /***
     * @Description: 获取所有脚本，树状展示
     * @Author: zrd
     * @Date: 2021/5/13 15:33
     * @param
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/script/{workSpaceId}")
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

    /***
     * @Description: 根据脚本id获取脚本
     * @Author: zrd
     * @Date: 2021/5/13 15:34
     * @param id
     * @return com.geominfo.mlsql.commons.Message
     */
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

    /***
     * @Description: 根据脚本名获取脚本日志
     * @Author: zrd
     * @Date: 2021/5/13 15:37
     * @param jobName 脚本名
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/getJobLog/{jobName}")
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
    @PostMapping("/saveScript")
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
    @DeleteMapping("/deleteDir/{resourceId}")
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
    @DeleteMapping("/deleteScript/{resourceId}")
    public Message deleteScript(@PathVariable BigDecimal resourceId){
        boolean mkdir = scriptService.deleteScript(resourceId);
        if (mkdir) {
            return new Message().ok("删除成功");
        }
        return new Message().error("删除失败");
    }
}
