package com.geominfo.mlsql.controller.share;

import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.base.BaseNewController;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.po.EngineInfo;
import com.geominfo.mlsql.domain.po.ShareInfo;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.ShareInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: geometry-bi
 * @description: 分享脚本操作接口
 * @author: LF
 * @create: 2021/7/5 17:56
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/share")
@Api(value="分享脚本操作接口",tags={"分享脚本操作接口"})
public class ShareInfoController extends BaseNewController {
    @Autowired
    private ShareInfoService shareInfoService;

    @ApiOperation(value="新增分享脚本信息", httpMethod = "POST")
    @PostMapping("/insert")
    public Message insertShareInfo(@RequestBody ShareInfo shareInfo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        logger.info("insertShareInfo param:{}", shareInfo);
        try {
            baseResultVo = shareInfoService.insertShareInfo(shareInfo);
            return baseResultVo.isSuccess()
                    ? new Message().ok(baseResultVo.getReturnMsg())
                    : new Message().error(baseResultVo.getReturnMsg());
        } catch (Exception e) {
            logger.error("insertShareInfo Exception", e);
            return new Message().error(InterfaceMsg.INSERT_ERROR.getMsg());
        }
    }
}
