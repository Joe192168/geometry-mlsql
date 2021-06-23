package com.geominfo.mlsql.controller.engine;

import com.geominfo.authing.common.constants.CommonConstants;
import com.geominfo.authing.common.enums.EnumOperateLogType;
import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.aop.GeometryLogAnno;
import com.geominfo.mlsql.base.BaseNewController;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.po.EngineInfo;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.EngineInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: geometry-bi
 * @description: 执行引擎管理类
 * @author: LF
 * @create: 2021/6/16 10:55
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/engine")
@Api(value="执行引擎接口管理",tags={"执行引擎接口管理"})
public class EngineInfoController extends BaseNewController {
    @Autowired
    private EngineInfoService engineInfoService;


    @ApiOperation(value="新增引擎信息", httpMethod = "POST")
    @PostMapping("/insert")
    @GeometryLogAnno(operateType = EnumOperateLogType.ENGINE_OPERATE)
    public Message insertEngineInfo(@RequestBody EngineInfo engineInfo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        logger.info("insertEngineInfo param:{}", engineInfo);
        try {
            baseResultVo = engineInfoService.insertEngineInfo(engineInfo);
            return baseResultVo.isSuccess()
                    ? new Message().ok(baseResultVo.getReturnMsg())
                    : new Message().error(baseResultVo.getReturnMsg());
        } catch (Exception e) {
            logger.error("insertEngineInfo Exception", e);
            return new Message().error(InterfaceMsg.INSERT_ERROR.getMsg());
        }
    }
    @ApiOperation(value="修改引擎信息", httpMethod = "PUT")
    @PutMapping("/update")
    public Message updateEngineInfo(@RequestBody EngineInfo engineInfo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        logger.info("updateEngineInfo param:{}", engineInfo);
        try {
            baseResultVo = engineInfoService.updateEngineInfo(engineInfo);
            return baseResultVo.isSuccess()
                    ? new Message().ok(baseResultVo.getReturnMsg())
                    : new Message().error(baseResultVo.getReturnMsg());
        } catch (Exception e) {
            logger.error("updateEngineInfo Exception", e);
            return new Message().error(InterfaceMsg.INSERT_ERROR.getMsg());
        }
    }
    @ApiOperation(value="删除引擎信息", httpMethod = "DELETE")
    @DeleteMapping("/delete/{id}")
    @ApiImplicitParam(name="id",value = "引擎id", dataType = "Integer", paramType = "path",required = true)
    public Message deleteEngineInfo(@PathVariable BigDecimal id) {
        Boolean flag = engineInfoService.deleteEngineInfo(id);
        if (flag) {
            return new Message().ok(InterfaceMsg.DELETE_SUCCESS.getMsg());
        }else {
            return new Message().error(InterfaceMsg.DELETE_SUCCESS.getMsg());

        }
    }

    @ApiOperation(value="获取引擎信息列表", httpMethod = "GET")
    @GetMapping("/getEngineInfos")
    public Message getEngineInfos() {
        try {
            List<EngineInfo> engineInfos = engineInfoService.getEngineInfos();
            return new Message().ok(InterfaceMsg.QUERY_SUCCESS.getMsg()).addData(CommonConstants.DATA,engineInfos);
        }catch (Exception e){
            logger.error("method # getEngineInfos exception", e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

}
