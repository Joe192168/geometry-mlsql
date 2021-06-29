package com.geominfo.mlsql.controller.homePage;

import com.geominfo.authing.common.constants.CommonConstants;
import com.geominfo.mlsql.base.BaseNewController;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.result.WorkSpaceInfoResult;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.SystemResourceService;
import com.geominfo.mlsql.services.WorkSpaceManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/23 18:02
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/index")
@Api(value = "首页接口管理", tags = {"首页接口管理"})
public class HomePageController extends BaseNewController {
    @Autowired
    private WorkSpaceManagerService workSpaceManagerService;
    @Autowired
    private SystemResourceService systemResourceService;

    @ApiOperation(value = "获取首页工作空间卡片", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer", paramType = "path", required = true)
    @GetMapping("/getWorkSpaceListCards/{userId}")
    public Message getWorkSpaceListCards(@PathVariable BigDecimal userId) {
        try {
            logger.info("getWorkSpaceListCards userId{} ", userId);
            List<WorkSpaceInfoResult> workSpaceInfoVos = workSpaceManagerService.getWorkSpaceLists(userId);
            return new Message().ok(InterfaceMsg.QUERY_SUCCESS.getMsg()).addData(CommonConstants.DATA, workSpaceInfoVos);
        } catch (Exception e) {
            logger.error("method # getWorkSpaceListCards exception", e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @ApiOperation(value = "获取首页查询最近相关脚本", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer", paramType = "path", required = true)
    @GetMapping("/getRecentlyScripts/{userId}")
    public Message getRecentlyScripts(@PathVariable BigDecimal userId) {
        try {
            logger.info("getRecentlyScripts userId{} ", userId);
            List<TSystemResources> systemResources = systemResourceService.getRecentlyScripts(userId);
            return new Message().ok(InterfaceMsg.QUERY_SUCCESS.getMsg()).addData(CommonConstants.DATA, systemResources);
        } catch (Exception e) {
            logger.error("method # getRecentlyScripts exception", e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }


}
