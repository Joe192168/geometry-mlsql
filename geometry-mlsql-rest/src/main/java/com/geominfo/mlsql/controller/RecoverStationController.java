package com.geominfo.mlsql.controller;

import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.services.RecoverStationService;
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
    @GetMapping("/getAllRecover/{workSpaceId}/{userId}")
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
    @PutMapping("/recoverResource/{resourceId}")
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
    @PutMapping("reChooseParentDir/{resourceId}/{parentId}")
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
    @DeleteMapping("deleteAll/{workSpaceId}/{userId}")
    public Message deleteAll(@PathVariable BigDecimal workSpaceId, @PathVariable BigDecimal userId) {
        boolean delete = recoverStationService.deleteAll(workSpaceId, userId);
        if (delete) {
            return new Message().ok("删除成功");
        }
        return new Message().error("删除失败");
    }
}
