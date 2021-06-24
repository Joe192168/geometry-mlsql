package com.geominfo.mlsql.controller.recoverStation;

import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.services.RecoverStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @title: RecoverStationController
 * @desc: 回收站controller
 * @date 2021/6/15 10:47
 */
@Api(value = "回收站接口",tags = {"回收站接口"})
@RestController
@RequestMapping("/recoverStation")
public class RecoverStationController {

    @Autowired
    private RecoverStationService recoverStationService;

    /***
     * @Description: 获取所有回收站文件
     * @Author: zrd
     * @Date: 2021/6/16 16:36
     * @param workSpaceId
     * @param userId
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "获取回收站所有文件接口",httpMethod = "GET")
    @GetMapping("/getAllRecover/{workSpaceId}/{userId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workSpaceId",value = "工作空间id",type = "BigDecimal",paramType = "path",required = true),
            @ApiImplicitParam(name = "userId",value = "用户id",type = "BigDecimal",paramType = "path",required = true),
    })
    public Message getAllRecover(@PathVariable BigDecimal workSpaceId, @PathVariable BigDecimal userId) {
        List<TSystemResources> list = recoverStationService.getAllRecoverResources(workSpaceId, userId);
        return new Message().ok().addData("data", list);
    }

    /***
     * @Description: 删除资源
     * @Author: zrd
     * @Date: 2021/6/16 16:36
     * @param resourceId
     * @return com.geominfo.mlsql.commons.Message
     */
    @DeleteMapping("/deleteResource/{resourceId}")
    @ApiOperation(value = "删除回收站资源接口")
    @ApiImplicitParam(name = "resourceId",value = "被删除资源id",type = "BigDecimal",paramType = "path",required = true)
    public Message deleteResource(@PathVariable BigDecimal resourceId) {
        boolean delete = recoverStationService.deleteResource(resourceId);
        if (delete) {
            return new Message().ok("删除成功");
        }
        return new Message().error("删除失败");
    }

    /***
     * @Description: 还原文件
     * @Author: zrd
     * @Date: 2021/6/16 16:35
     * @param resourceId
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "还原文件接口")
    @PutMapping("/recoverResource/{resourceId}")
    @ApiImplicitParam(name = "resourceId",value = "被还原文件资源id",type = "BigDecimal",paramType = "path",required = true)
    public Message recoverResource(@PathVariable BigDecimal resourceId) {
        boolean b = recoverStationService.recoverResource(resourceId);
        if (b) {
            return new Message().ok("还原成功");
        }
        return new Message().error("文件上级目录不存在请选择上级目录");
    }

    /***
     * @Description: 重新选择删除文件父级目录
     * @Author: zrd
     * @Date: 2021/6/16 16:35
     * @param resourceId
     * @param parentId
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "重新选择删除文件父级目录接口")
    @PutMapping("reChooseParentDir/{resourceId}/{parentId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceId",value = "被还原文件资源id",type = "BigDecimal",paramType = "path",required = true),
            @ApiImplicitParam(name = "parentId",value = "父级目录资源id",type = "BigDecimal",paramType = "path",required = true)
    }
    )
    public Message reChooseParentDir(@PathVariable BigDecimal resourceId, @PathVariable BigDecimal parentId) {
        boolean reChoose = recoverStationService.reChoosePatentDir(resourceId, parentId);
        if (reChoose) {
            return new Message().ok("还原成功");
        }
        return new Message().error("还原失败");
    }

    /***
     * @Description: 清空回收站
     * @Author: zrd
     * @Date: 2021/6/16 16:35
     * @param workSpaceId
     * @param userId
     * @return com.geominfo.mlsql.commons.Message
     */
    @ApiOperation(value = "清空回收站接口")
    @DeleteMapping("deleteAll/{workSpaceId}/{userId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workSpaceId",value = "工作空间id",type = "BigDecimal",paramType = "path",required = true),
            @ApiImplicitParam(name = "userId",value = "用户id",type = "BigDecimal",paramType = "path",required = true)
    })
    public Message deleteAll(@PathVariable BigDecimal workSpaceId, @PathVariable BigDecimal userId) {
        boolean delete = recoverStationService.deleteAll(workSpaceId, userId);
        if (delete) {
            return new Message().ok("删除成功");
        }
        return new Message().error("删除失败");
    }
}
