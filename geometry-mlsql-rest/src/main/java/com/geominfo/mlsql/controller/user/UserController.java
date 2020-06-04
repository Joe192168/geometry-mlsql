package com.geominfo.mlsql.controller.user;

import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.utils.MD5Scala;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: console demo
 * @description:
 * @author: anan
 * @create: 2020-06-02 14:55
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/user")
@Api(value="用户接口类",tags={"用户接口"})
@Log4j2
public class UserController {
    @Autowired
    private Message message ;

    @Autowired
    private UserService userService;


    @RequestMapping("/login")
    @ApiOperation(value = "用户登录接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名",name = "userName",dataType = "String",paramType = "query",required = true),
            @ApiImplicitParam(value = "密码",name = "password",dataType = "String",paramType = "query",required = true)
    })
    public Message userLogin(@RequestParam(value = "userName", required = true) String userName,
                              @RequestParam(value = "password", required = true) String password){
        MlsqlUser user =  userService.userLogin(userName);

        if(user == null){
            return message.error(400,"msg:" + userName + "is not exists");
        }
        if (!user.getPassword().equals(MD5Scala.md5(password))) {
            return message.error(400,"msg:password  is not correct");
        }

        if (user.getStatus()!=null && user.getStatus().equals("MlsqlUser.STATUS_LOCK")) {
            return message.error(400,"msg:" + userName + "has been lock");
        }

        return message.ok(200,"用户登录成功").addData("data",user);
    }




    @RequestMapping("/allUsers")
    @ApiOperation(value = "获取所有用戶", httpMethod = "GET")
    public Message findAll(){
        List<MlsqlUser> user =  userService.getAllUsers();
        if(user != null){
            return message.ok(200,"获取所有用户成功").addData("data",user);
        }else{
            return message.error(200,"no user exists");
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
        return message.ok(200,"获取用户成功").addData("data",user);
    }
}
