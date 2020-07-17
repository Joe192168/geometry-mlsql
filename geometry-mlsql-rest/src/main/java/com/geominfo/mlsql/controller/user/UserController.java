package com.geominfo.mlsql.controller.user;

import com.alibaba.fastjson.JSON;
import com.geominfo.mlsql.domain.vo.JwtAccount;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.service.user.TeamRoleService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.util.JsonWebTokenUtil;
import com.geominfo.mlsql.utils.MD5Scala;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TeamRoleService teamRoleService;


    @RequestMapping("/login")
    @ApiOperation(value = "用户登录接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名",name = "userName",dataType = "String",paramType = "query",required = true),
            @ApiImplicitParam(value = "密码",name = "password",dataType = "String",paramType = "query",required = true)
    })
    public Message userLogin(@RequestParam(value = "userName", required = true) String userName,
                              @RequestParam(value = "password", required = true) String password){
        MlsqlUser user =  userService.getUserByName(userName);

        if(user == null){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + userName + "is not exists");
        }
        if (!user.getPassword().equals(MD5Scala.md5(password))) {
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS,"msg:password  is not correct");
        }

        if (user.getStatus()!=null && user.getStatus().equals(MlsqlUser.STATUS_LOCK)) {
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + userName + "has been lock");
        }

        String appId =  GlobalConstant.APP_NAME;
        String jwt = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), appId,
                GlobalConstant.TOKEN_SERVER, JsonWebTokenUtil.refreshPeriodTime >> GlobalConstant.DEFAULT_INT_VALUE, JSON.toJSONString(user), user.getName());
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"user login success").addData("data",jwt);
    }
    @RequestMapping("/allUsers")
    @ApiOperation(value = "获取所有用戶", httpMethod = "GET")
    public Message findAll(){
        Map<String,Object> map = new HashMap<String,Object>();
        List<MlsqlUser> user =  userService.getAllUsers(map);
        if(user != null){
            return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get user success").addData("data",user);
        }else{
            return new Message().error(ReturnCode.RETURN_SUCCESS_STATUS,"no user exists");
        }

    }

    @RequestMapping("/allUsers/page")
    @ApiOperation(value = "分页获取用户信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页",name = "pageNum",dataType = "Int",paramType = "query",required = true),
            @ApiImplicitParam(value = "页面容量",name = "pageSize",dataType = "Int",paramType = "query",required = true)
    })
    public Message findAllByPage(@RequestParam(value = "pageNum", required = true) int pageNum
            ,@RequestParam(value = "pageSize", required = true) int pageSize){
        List<MlsqlUser> user =  userService.getUserByPage(pageNum, pageSize);
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get user success").addData("data",user);
    }

    @RequestMapping("/register")
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
                return new Message().error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + ReturnCode.TEAM_EXISTS);
            }
            return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"register success").addData("data",user);
        }
        return new Message().error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + ReturnCode.USER_EXISTS);
    }

    @RequestMapping("/userName")
    @ApiOperation(value = "通过用户名获取用户信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名",name = "userName",dataType = "String",paramType = "query",required = true)
    })
    public Message getUserByName(@RequestParam(value = "userName", required = true) String userName){
        MlsqlUser user = userService.getUserByName(userName);
        if(user.getStatus()!=null && user.getStatus().equals(MlsqlUser.STATUS_LOCK)){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + userName + "has been lock");
        }
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get user success").addData("data",user);
    }

    @RequestMapping("/test")
    @ApiOperation(value = "测试token", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名",name = "userName",dataType = "String",paramType = "query",required = true)
    })
    public Message test(HttpServletRequest request, HttpServletResponse response){
        JwtAccount jwtAccount = JsonWebTokenUtil.parseJwt(request.getHeader("jwt"));
        log.info(jwtAccount.getAppId());
        log.info(jwtAccount.getRoles());
        log.info(jwtAccount.getIssuedAt());
        log.info(JsonWebTokenUtil.parseJwtPayload(request.getHeader("jwt")));
        return null;
    }

    @RequestMapping("/tags/update")
    @ApiOperation(value = "后端绑定更新", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "绑定tags",name = "backendTags",dataType = "String",paramType = "query",required = true)
    })
    public Message tagsUpdate(@RequestParam(value = "backendTags") String backendTags,
                              @RequestParam(value = "append") boolean append){
        MlsqlUser user = userService.getUserByName("awh18@gmail.com"); //replace token
        if(append){
            user.setBackendTags(backendTags);
        }else{
            user.setBackendTags((user.getBackendTags() == null ?"":user.getBackendTags() + ",") + backendTags);
        }
        userService.updateUser(user);
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"update user backendTags sucess").addData("data",user);
    }

    @RequestMapping("/changepassword")
    @ApiOperation(value = "修改密码", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "新密码",name = "newPassword",dataType = "String",paramType = "query",required = true),
            @ApiImplicitParam(value = "旧密码",name = "oldPassword",dataType = "String",paramType = "query",required = true)
    })
    public Message changePassword(@RequestParam(value = "newPassword", required = true) String newPassword
            ,@RequestParam(value = "userName", required = true) String userName
            ,@RequestParam(value = "oldPassword", required = true) String oldPassword){
        MlsqlUser user =  userService.getUserByName(userName);
        if(user.getStatus()!=null && user.getStatus().equals(MlsqlUser.STATUS_LOCK)){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS,"msg:" + userName + "has been lock");
        }
        if(user.getStatus()!=null && user.getStatus().equals(MlsqlUser.STATUS_PAUSE)){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS,"msg:userName:" + userName + "has been paused");
        }

        if(user.getPassword().equals(MD5Scala.md5(oldPassword))){
            userService.changePassword(userName, MD5Scala.md5(newPassword));
            return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,",msg:change password success");
        }else
        {
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS,"msg:change password fail");
        }
    }
}
