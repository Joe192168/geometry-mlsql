package com.geominfo.mlsql.controller.account;

import com.geominfo.mlsql.base.BaseNewController;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.vo.QueryUserVo;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.AuthApiService;
import com.geominfo.mlsql.utils.FeignUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpStatus;
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
@RequestMapping("/user")
public class UserInfoController extends BaseNewController {

    @Autowired
    private AuthApiService authApiService;

    @ApiOperation(value = "根据Id查询人员信息", httpMethod = "GET", notes = "查询人员信息。")
    @ApiImplicitParam(name = "id", value = "人员id", dataType = "Integer", paramType = "path")
    @GetMapping("/getUserById/{id}")
    public Message getUserById(@PathVariable BigDecimal id) {
        logger.info("人员id: {}",id);
        try {
            return authApiService.getUserById(id);
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
            return authApiService.getUserByLoginName(loginName);
        }catch (Exception e){
            logger.error("查询人员信息时，发生异常！",e);
            return new Message().error(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    @GetMapping("/getAllUsers")
    @ApiOperation(value = "查询所有用户", httpMethod = "GET")
    public Message getAllUsers() {
        try {
            List<User> users = FeignUtils.parseArray(authApiService.getUsers(),User.class);
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
            return authApiService.getUsersPage(queryUserVo);
        } catch (Exception e) {
            logger.error("查询人员列表(分页)时发生异常!", e);
            return new Message().error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "查询人员列表(分页)时发生异常，请稍后重试！");
        }
    }

}
