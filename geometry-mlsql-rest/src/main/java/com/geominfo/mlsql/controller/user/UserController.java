package com.geominfo.mlsql.controller.user;

import com.alibaba.fastjson.JSON;
import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.JwtAccount;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlGroup;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.service.user.TeamRoleService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import com.geominfo.mlsql.systemidentification.SystemCustomIdentification;
import com.geominfo.mlsql.util.JsonWebTokenUtil;
import com.geominfo.mlsql.util.RequestResponseUtil;
import com.geominfo.mlsql.utils.MD5Scala;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @program: console demo
 * @description:
 * @author: anan
 * @create: 2020-06-02 14:55
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/api_v1/user")
@Api(value="用户账户及操作类",tags={"用户账户及操作接口"})
@Log4j2
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private TeamRoleService teamRoleService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "POST用户登录签发JWT")
    public Message userLogin(HttpServletRequest request){
        Map<String, String> params = RequestResponseUtil.getRequestBodyMap(request);
        String productId = params.get(SystemCustomIdentification.PRODUCT_ID);
        if (org.apache.commons.lang3.StringUtils.isBlank(productId)){
            productId = GlobalConstant.BIXT_ROOT;
        }
        String appId = params.get(SystemCustomIdentification.LOGIN_NAME);
        MlsqlUser user =  userService.getUserByName(appId);
        if (user.getStatus()!=null && user.getStatus().equals(MlsqlUser.STATUS_LOCK)) {
            return error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + appId + "has been lock");
        }
        // 根据用户登录名查询用户所拥有权限资源id
        String permissions = null;
        //根据用户登录名查询用户所拥有的所有角色
        String roles = teamRoleService.getRolesByUserName(appId);
        String jwt = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), appId,
                GlobalConstant.TOKEN_SERVER, JsonWebTokenUtil.refreshPeriodTime >> GlobalConstant.DEFAULT_INT_VALUE, roles, permissions);
        // 将签发的JWT存储到Redis： {JWT-SESSION-{appID} , jwt}
        redisTemplate.opsForValue().set(SystemCustomIdentification.JWT_SESSION + appId, jwt, JsonWebTokenUtil.refreshPeriodTime, TimeUnit.SECONDS);
        return success(1003, "issue jwt success").addData(SystemCustomIdentification.JWT, jwt)
                .addData(SystemCustomIdentification.USER, user);
    }

    @RequestMapping(value = "/allUsers", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有用戶", httpMethod = "GET")
    public Message findAll(ServletRequest servletRequest){
        Map<String,Object> map = new HashMap<String,Object>();
        List<MlsqlUser> user =  userService.getAllUsers(map);
        if(user != null){
            return success(ReturnCode.RETURN_SUCCESS_STATUS,"get user success").addData("data",user);
        }else{
            return error(ReturnCode.RETURN_SUCCESS_STATUS,"no user exists");
        }

    }

    @RequestMapping(value = "/allUsers/page", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取用户信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页",name = "pageNum",dataType = "Int",paramType = "query",required = true),
            @ApiImplicitParam(value = "页面容量",name = "pageSize",dataType = "Int",paramType = "query",required = true)
    })
    public Message findAllByPage(@RequestParam(value = "pageNum", required = true) int pageNum
            ,@RequestParam(value = "pageSize", required = true) int pageSize){
        List<MlsqlUser> user =  userService.getUserByPage(pageNum, pageSize);
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"get user success").addData("data",user);
    }

    @RequestMapping(value= "/register", method = RequestMethod.POST)
    @ApiOperation(value = "用户注册", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名",name = "userName",dataType = "String",paramType = "query",required = true),
            @ApiImplicitParam(value = "密码",name = "password",dataType = "String",paramType = "query",required = true)
    })
    public Message register(@RequestParam(value = "userName", required = true) String userName,
                            @RequestParam(value = "password", required = true) String password){
        if(userService.getUserByName(userName) == null){
            MlsqlUser user = new MlsqlUser();
            user.setName(userName);
            user.setPassword(MD5Scala.md5(password));
            user.setRole("developer");
            Map<String,Object> map = new HashMap<String,Object>();
            if(userService.getAllUsers(map).size() == 0){
                user.setRole("admin");
            }
            userService.register(user);
            //create user default group and roles
            String uuid = UUID.randomUUID().toString();
            if(teamRoleService.checkTeamNameValid(uuid)){
                teamRoleService.createDefaultTeamAndRole(user, uuid);
            }else{
                return error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + InterfaceReturnInformation.TEAM_EXISTS);
            }
            return success(ReturnCode.RETURN_SUCCESS_STATUS,"register success").addData("data",user);
        }
        return error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + InterfaceReturnInformation.USER_EXISTS);
    }

    @RequestMapping(value = "/userName", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户", httpMethod = "POST")
    public Message getUserByName(){
        MlsqlUser user = userService.getUserByName(userName);
        if(user.getStatus()!=null && user.getStatus().equals(MlsqlUser.STATUS_LOCK)){
            return error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + user.getName() + "has been lock");
        }
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"get user success").addData("data",user);
    }

    @RequestMapping("/tags/update")
    @ApiOperation(value = "后端绑定更新", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "绑定tags",name = "backendTags",dataType = "String",paramType = "query",required = true)
    })
    public Message tagsUpdate(@RequestParam(value = "backendTags") String backendTags,
                              @RequestParam(value = "append") boolean append){
        MlsqlUser user = userService.getUserByName(userName);
        if(append){
            user.setBackendTags(backendTags);
        }else{
            user.setBackendTags((user.getBackendTags() == null ?"":user.getBackendTags() + ",") + backendTags);
        }
        userService.updateUser(user);
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"update user backendTags sucess").addData("data",user);
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "新密码",name = "newPassword",dataType = "String",paramType = "query",required = true),
            @ApiImplicitParam(value = "旧密码",name = "oldPassword",dataType = "String",paramType = "query",required = true)
    })
    public Message changePassword(@RequestParam(value = "newPassword", required = true) String newPassword
            ,@RequestParam(value = "oldPassword", required = true) String oldPassword){
        MlsqlUser user =  userService.getUserByName(userName);
        if(user.getStatus()!=null && user.getStatus().equals(MlsqlUser.STATUS_LOCK)){
            return error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + userName + "has been lock");
        }
        if(user.getStatus()!=null && user.getStatus().equals(MlsqlUser.STATUS_PAUSE)){
            return error(ReturnCode.RETURN_ERROR_STATUS,"msg:userName:" + userName + "has been paused");
        }

        if(user.getPassword().equals(MD5Scala.md5(oldPassword))){
            userService.changePassword(userName, MD5Scala.md5(newPassword));
            return success(ReturnCode.RETURN_SUCCESS_STATUS,",msg:change password success");
        }else
        {
            return error(ReturnCode.RETURN_ERROR_STATUS,"msg:change password fail");
        }
    }
}
