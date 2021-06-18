package com.geominfo.mlsql.controller.spacemanager;

import com.geominfo.authing.common.constants.CommonConstants;
import com.geominfo.authing.common.enums.EnumOperateLogType;
import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.aop.GeometryLogAnno;
import com.geominfo.mlsql.base.BaseNewController;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.po.EngineInfo;
import com.geominfo.mlsql.domain.po.WorkSpaceEngine;
import com.geominfo.mlsql.domain.vo.SpaceMemberVo;
import com.geominfo.mlsql.domain.vo.WorkSpaceInfoVo;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.EngineInfoService;
import com.geominfo.mlsql.services.WorkSpaceManagerService;
import com.geominfo.mlsql.services.WorkSpaceMemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/5/20 15:30
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/workSpace")
public class WorkSpaceManageController extends BaseNewController {

    @Autowired
    private WorkSpaceManagerService workSpaceManagerService;
    @Autowired
    private WorkSpaceMemberService workSpaceMemberService;
    @Autowired
    private EngineInfoService engineInfoService;

    @ApiOperation(value="新增工作空间", httpMethod = "POST")
    @PostMapping("/insert")
    @GeometryLogAnno(operateType = EnumOperateLogType.WORKSPACE_OPERATE)
    public Message insertWorkSpace(@RequestBody WorkSpaceInfoVo workSpaceInfoVo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        logger.info("insertWorkSpace param:{}", workSpaceInfoVo);
        try {
            baseResultVo = workSpaceManagerService.insertWorkSpace(workSpaceInfoVo);
            return baseResultVo.isSuccess()
                    ? new Message().ok(baseResultVo.getReturnMsg())
                    : new Message().error(baseResultVo.getReturnMsg());
        } catch (Exception e) {
            logger.error("insertWorkSpace Exception", e);
            return new Message().error(InterfaceMsg.INSERT_ERROR.getMsg());
        }
    }

    @ApiOperation(value="修改工作空间", httpMethod = "PUT")
    @PutMapping("/update")
    public Message updateWorkSpace(@RequestBody WorkSpaceInfoVo workSpaceInfoVo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        logger.info("updateWorkSpace param:{}", workSpaceInfoVo);
        try {
            baseResultVo = workSpaceManagerService.updateWorkSpace(workSpaceInfoVo);
            return baseResultVo.isSuccess()
                    ? new Message().ok(baseResultVo.getReturnMsg())
                    : new Message().error(baseResultVo.getReturnMsg());
        } catch (Exception e) {
            logger.error("updateWorkSpace Exception", e);
            return new Message().error(InterfaceMsg.INSERT_ERROR.getMsg());
        }
    }
    @ApiOperation(value="删除工作空间", httpMethod = "DELETE")
    @DeleteMapping("/delete/{id}")
    public Message deleteWorkSpace(@PathVariable BigDecimal id) {
        Boolean flag = workSpaceManagerService.deleteWorkSpace(id);
        if (flag) {
            return new Message().ok(InterfaceMsg.DELETE_SUCCESS.getMsg());
        }else {
            return new Message().error(InterfaceMsg.DELETE_SUCCESS.getMsg());

        }
    }

    @ApiOperation(value="设置默认空间",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="spaceId",value = "工作空间id", dataType = "int", paramType = "path",required = true),
            @ApiImplicitParam(name="userId",value = "用户id", dataType = "int", paramType = "path",required = true)
    })
    @GetMapping("/setDefaultSpace/{spaceId}/{userId}")
    public Message setDefaultWorkSpace(@PathVariable BigDecimal spaceId, @PathVariable BigDecimal userId){
        BaseResultVo baseResultVo = new BaseResultVo();
        try {
            logger.info("setDefaultWorkSpace spaceId {},userId{} ",spaceId,userId);
            baseResultVo = workSpaceManagerService.setDefaultWorkSpace(spaceId,userId);
            return baseResultVo.isSuccess()
                    ? new Message().ok(baseResultVo.getReturnMsg())
                    : new Message().error(baseResultVo.getReturnMsg());
        }catch (Exception e){
            logger.error("method # setDefaultWorkSpace exception", e);
            return new Message().error(InterfaceMsg.SET_DEFAULT_SPACE_ERROR.getMsg());
        }
    }

    @ApiOperation(value="转让工作空间",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="spaceId",value = "工作空间id", dataType = "int", paramType = "path",required = true),
            @ApiImplicitParam(name="userId",value = "用户id", dataType = "int", paramType = "path",required = true)
    })
    @GetMapping("/transferWorkSpace/{spaceId}/{userId}")
    public Message transferWorkSpace(@PathVariable BigDecimal spaceId, @PathVariable BigDecimal userId){
        BaseResultVo baseResultVo = new BaseResultVo();
        try {
            logger.info("setDefaultWorkSpace spaceId {},userId{} ",spaceId,userId);
            baseResultVo = workSpaceManagerService.transferWorkSpace(spaceId,userId);
            return baseResultVo.isSuccess()
                    ? new Message().ok(baseResultVo.getReturnMsg())
                    : new Message().error(baseResultVo.getReturnMsg());
        }catch (Exception e){
            logger.error("method # transferWorkSpace exception ", e);
            return new Message().error(InterfaceMsg.SET_DEFAULT_SPACE_ERROR.getMsg());
        }
    }

    @ApiOperation(value="新增空间成员", httpMethod = "POST")
    @PostMapping("/insertSpaceMember")
    @GeometryLogAnno(operateType = EnumOperateLogType.WORKSPACE_OPERATE)
    public Message insertSpaceMember(@RequestBody WorkSpaceInfoVo workSpaceInfoVo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        logger.info("insertSpaceMember param:{}", workSpaceInfoVo);
        try {
            baseResultVo = workSpaceMemberService.insertSpaceMember(workSpaceInfoVo);
            return baseResultVo.isSuccess()
                    ? new Message().ok(baseResultVo.getReturnMsg())
                    : new Message().error(baseResultVo.getReturnMsg());
        } catch (Exception e) {
            logger.error("insertSpaceMember Exception", e);
            return new Message().error(InterfaceMsg.INSERT_ERROR.getMsg());
        }
    }

    @ApiOperation(value="删除空间成员", httpMethod = "DELETE")
    @DeleteMapping("/deleteSpaceMember/{spaceId}/{userId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="spaceId",value = "工作空间id", dataType = "int", paramType = "path",required = true),
            @ApiImplicitParam(name="userId",value = "用户id", dataType = "int", paramType = "path",required = true)
    })
    public Message deleteSpaceMember(@PathVariable BigDecimal spaceId,@PathVariable BigDecimal userId) {
        Boolean flag = workSpaceMemberService.deleteSpaceMember(spaceId,userId);
        if (flag) {
            return new Message().ok(InterfaceMsg.DELETE_SUCCESS.getMsg());
        }else {
            return new Message().error(InterfaceMsg.DELETE_SUCCESS.getMsg());

        }
    }

    @ApiOperation(value="查询工作空间列表",httpMethod = "GET")
    @GetMapping("/getWorkSpaceLists/{userId}")
    public Message getWorkSpaceLists( @PathVariable BigDecimal userId){
        try {
            logger.info("getWorkSpaceLists userId{} ",userId);
            List<WorkSpaceInfoVo> workSpaceInfoVos = workSpaceManagerService.getWorkSpaceLists(userId);
            return new Message().ok(InterfaceMsg.QUERY_SUCCESS.getMsg()).addData(CommonConstants.DATA,workSpaceInfoVos);
        }catch (Exception e){
            logger.error("method # getWorkSpaceLists exception", e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @ApiOperation(value = "查询空间成员列表",httpMethod = "GET")
    @GetMapping("/getSpaceMemberBySpaceId/{spaceId}")
    public Message getSpaceMemberBySpaceId(@PathVariable BigDecimal spaceId){
        try {
            logger.info("getSpaceMemberBySpaceId spaceId:{} ",spaceId);
            List<SpaceMemberVo> spaceMemberVos = workSpaceMemberService.getSpaceMemberBySpaceId(spaceId);
            return new Message().ok(InterfaceMsg.QUERY_SUCCESS.getMsg()).addData(CommonConstants.DATA,spaceMemberVos);
        }catch (Exception e){
            logger.error("method # getSpaceMemberBySpaceId exception", e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @ApiOperation(value = "查询可转让的空间成员",httpMethod = "GET")
    @GetMapping("/getTransferMemberBySpaceId/{spaceId}")
    public Message getTransferMemberBySpaceId(@PathVariable BigDecimal spaceId){
        try {
            logger.info("getTransferMemberBySpaceId spaceId:{}",spaceId);
            List<SpaceMemberVo> spaceMemberVos = workSpaceMemberService.getTransferMemberBySpaceId(spaceId);
            return new Message().ok(InterfaceMsg.QUERY_SUCCESS.getMsg()).addData(CommonConstants.DATA,spaceMemberVos);
        }catch (Exception e){
            logger.error("method # getTransferMemberBySpaceId exception", e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @ApiOperation(value="根据名称查询工作空间列表",httpMethod = "GET")
    @GetMapping("/getWorkSpaceListsByName/{userId}/{spaceName}")
    public Message getWorkSpaceListsByName( @PathVariable BigDecimal userId,@PathVariable String spaceName){
        try {
            logger.info("getWorkSpaceListsByName userId{},spaceName{} ",userId,spaceName);
            List<WorkSpaceInfoVo> workSpaceInfoVos = workSpaceManagerService.getWorkSpaceLists(userId);
            return new Message().ok(InterfaceMsg.QUERY_SUCCESS.getMsg()).addData(CommonConstants.DATA,workSpaceInfoVos);
        }catch (Exception e){
            logger.error("method # getWorkSpaceListsByName exception", e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @ApiOperation(value = "查询可添加的引擎配置",httpMethod = "GET")
    @GetMapping("/getEnginesBySpaceId/{spaceId}")
    public Message getEnginesBySpaceId(@PathVariable BigDecimal spaceId){
        try {
            logger.info("getEnginesBySpaceId spaceId:{}",spaceId);
            List<EngineInfo> engineInfos = engineInfoService.getEnginesBySpaceId(spaceId);
            return new Message().ok(InterfaceMsg.QUERY_SUCCESS.getMsg()).addData(CommonConstants.DATA,engineInfos);
        }catch (Exception e){
            logger.error("method # getEnginesBySpaceId exception", e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @ApiOperation(value = "根据工作空间id，查询所有的配置引擎列表",httpMethod = "GET")
    @GetMapping("/getEngineLists/{spaceId}")
    public Message getEngineLists(@PathVariable BigDecimal spaceId){
        try {
            logger.info("getEngineLists spaceId:{}",spaceId);
            List<EngineInfo> engineInfos = engineInfoService.getEngineLists(spaceId);
            return new Message().ok(InterfaceMsg.QUERY_SUCCESS.getMsg()).addData(CommonConstants.DATA,engineInfos);
        }catch (Exception e){
            logger.error("method # getEngineLists exception", e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @ApiOperation(value="删除工作空间引擎配置", httpMethod = "DELETE")
    @DeleteMapping("/delete/{engineId}/{spaceId}")
    public Message deleteWorkSpace(@PathVariable BigDecimal engineId,@PathVariable BigDecimal spaceId) {
        try {
            workSpaceManagerService.deleteEngine(engineId,spaceId);
            return new Message().ok(InterfaceMsg.DELETE_SUCCESS.getMsg());
        }catch (Exception e){
            return new Message().error(InterfaceMsg.DELETE_SUCCESS.getMsg());
        }

    }

    @ApiOperation(value="给工作空间新增引擎配置", httpMethod = "POST")
    @PostMapping("/insertWorkSpaceEngine")
    @GeometryLogAnno(operateType = EnumOperateLogType.WORKSPACE_OPERATE)
    public Message insertWorkSpaceEngine(@RequestBody WorkSpaceEngine workSpaceEngine) {
        BaseResultVo baseResultVo = new BaseResultVo();
        logger.info("insertWorkSpaceEngine param:{}", workSpaceEngine);
        try {
            baseResultVo = workSpaceManagerService.insertWorkSpaceEngine(workSpaceEngine);
            return baseResultVo.isSuccess()
                    ? new Message().ok(baseResultVo.getReturnMsg())
                    : new Message().error(baseResultVo.getReturnMsg());
        } catch (Exception e) {
            logger.error("insertWorkSpaceEngine Exception", e);
            return new Message().error(InterfaceMsg.INSERT_ERROR.getMsg());
        }
    }

    @ApiOperation(value="设置工作空间默认引擎", httpMethod = "PUT")
    @PutMapping("/setDefaultEngine/{engineId}/{spaceId}")
    public Message setDefaultEngine(@PathVariable BigDecimal engineId,@PathVariable BigDecimal spaceId) {
        BaseResultVo baseResultVo = new BaseResultVo();
        logger.info("setDefaultEngine param:{}", engineId,spaceId);
        try {
            baseResultVo = workSpaceManagerService.setDefaultEngine(engineId,spaceId);
            return baseResultVo.isSuccess()
                    ? new Message().ok(baseResultVo.getReturnMsg())
                    : new Message().error(baseResultVo.getReturnMsg());
        } catch (Exception e) {
            logger.error("setDefaultEngine Exception", e);
            return new Message().error(InterfaceMsg.INSERT_ERROR.getMsg());
        }
    }

}
