package com.geominfo.mlsql.controller.app;

import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.service.app.MlsqlKvService;
import com.geominfo.mlsql.service.app.impl.MlsqlKvServiceImpl;
import com.geominfo.mlsql.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 应用控制类
 * @author: ryan(丁帅波)
 * @create: 2020-11-30 11：50
 * @version: 1.0.0
 */
@RestController
@Api(value = "应用各种状态控制类",tags = {"应用各种状态控制类"})
@Log4j2
public class AppController extends BaseController {

    @Autowired
    private MlsqlKvService mlsqlKvService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api_v1/app", method = {RequestMethod.POST,RequestMethod.GET})
    public Message app(){
        Map<String, Boolean> map = mlsqlKvService.appInfo();
        return success(HttpStatus.SC_OK,"get success").addData("data",map);
    }


    @RequestMapping(value = "/api_v1/app/save", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "login", value = "登录", required =  false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "register", value = "注册", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "console", value = "console", required = false, paramType = "query", dataType = "String")
    })
    public Message appSave(@RequestParam(value = "login", required = false) String login,
                           @RequestParam(value = "register", required = false) String register,
                           @RequestParam(value = "console", required = false) String console){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        if(mlsqlUser.getRole().equals("admin")){
            return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,"requirement failed: admin is required");
        }
        Map<String, Boolean> map = mlsqlKvService.appInfo();
        if (!StringUtils.isBlank(login)){
            Boolean loginFlag = map.get("login");
            if (loginFlag != null){
                mlsqlKvService.updateApp(MlsqlKvServiceImpl.LOGIN,login);
            }else {
                mlsqlKvService.addApp(MlsqlKvServiceImpl.LOGIN,login);
            }
        }

        if (!StringUtils.isBlank(register)){
            Boolean registerFlag = map.get("register");
            if (registerFlag != null){
                mlsqlKvService.updateApp(MlsqlKvServiceImpl.REGISTER,register);
            }else {
                mlsqlKvService.addApp(MlsqlKvServiceImpl.REGISTER,register);
            }
        }

        if (!StringUtils.isBlank(console)){
            Boolean consoleFlag = map.get("console");
            if (consoleFlag != null){
                mlsqlKvService.updateApp(MlsqlKvServiceImpl.CONSOLE,console);
            }else {
                mlsqlKvService.addApp(MlsqlKvServiceImpl.CONSOLE,console);
            }
        }

        return success(HttpStatus.SC_OK, "success");
    }
}
