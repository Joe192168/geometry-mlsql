package com.geominfo.mlsql.controller.account;

import com.geominfo.authing.common.enums.EnumOperateLogType;
import com.geominfo.mlsql.aop.GeometryLogAnno;
import com.geominfo.mlsql.base.BaseNewController;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.commons.SystemCustomIdentification;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.vo.QueryUserVo;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.AuthQueryApiService;
import com.geominfo.mlsql.utils.FeignUtils;
import com.geominfo.mlsql.utils.RequestResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/5/20 15:30
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/account")
@Api(value="用户账户操作接口",tags={"用户账户操作接口"})
public class AccountController extends BaseNewController {


    @Autowired
    private AuthQueryApiService authQueryApiService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GeometryLogAnno(operateType = EnumOperateLogType.MLSQL_EXIT_OPERATE)
    @ApiOperation(value = "用户登出", httpMethod = "POST")
    @PostMapping("/exit")
    public Message accountExit(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String > bodyMap = RequestResponseUtil.getRequestBodyMap(request);
        String appId = bodyMap.get("appId");
        if (StringUtils.isEmpty(appId)) {
            return new Message().error(500, "用户未登录无法登出");
        }
        Map<String,String > headerMap = RequestResponseUtil.getRequestHeaders(request);
        String tokenId = headerMap.get("tokenid");
        String jwt = redisTemplate.opsForValue().get("token:"+tokenId);
        if (StringUtils.isEmpty(jwt)) {
            return new Message().error(500, "用户未登录无法登出");
        }
        redisTemplate.opsForValue().getOperations().delete(SystemCustomIdentification.TOKEN_FOLDER+tokenId);
        return new Message().ok(200, "用户退出成功");
    }

    @ApiOperation(value = "根据Id查询人员信息", httpMethod = "GET", notes = "查询人员信息。")
    @ApiImplicitParam(name = "id", value = "人员id", dataType = "Integer", paramType = "path")
    @GetMapping("/getUserById/{id}")
    public Message getUserById(@PathVariable BigDecimal id) {
        logger.info("人员id: {}",id);
        try {
            return authQueryApiService.getUserById(id);
        }catch (Exception e){
            logger.error("查询人员信息时，发生异常！",e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @ApiOperation(value = "根据人员名称查询人员信息", httpMethod = "GET", notes = "查询人员信息。")
    @ApiImplicitParam(name = "loginName", value = "登录名", dataType = "String", paramType = "path")
    @GetMapping("/getUserByLoginName/{loginName}")
    public Message getUserByLoginName(@PathVariable String loginName) {
        logger.info("账户名称: {}",loginName);
        try {
            return authQueryApiService.getUserByLoginName(loginName);
        }catch (Exception e){
            logger.error("查询人员信息时，发生异常！",e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @GetMapping("/getAllUsers")
    @ApiOperation(value = "查询所有用户", httpMethod = "GET")
    public Message getAllUsers() {
        try {
            List<User> users = FeignUtils.parseArray(authQueryApiService.getUsers(),User.class);
            return new Message().ok().addData("userList", users);
        } catch (Exception e) {
            logger.error("查询所有用户时发生异常！", e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @ApiOperation(value = "查询人员列表(分页)", httpMethod = "POST")
    @PostMapping("/getUsersPage")
    public Message getUsersPage(@RequestBody QueryUserVo queryUserVo) {
        logger.info("查询人员列表(分页)，条件：{}", queryUserVo);
        try {
            return authQueryApiService.getUsersPage(queryUserVo);
        } catch (Exception e) {
            logger.error("查询人员列表(分页)时发生异常!", e);
            return new Message().error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "查询人员列表(分页)时发生异常，请稍后重试！");
        }
    }


}
