package com.geominfo.mlsql.controller;

import com.geominfo.mlsql.commons.Result;
import com.geominfo.mlsql.commons.ResultCode;
import com.geominfo.mlsql.services.impl.URLFilterInvocationSecurityMetadataSourceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@Api(value="用户账户controller",tags={"用户账户操作接口"})
public class AccountController {

    @Autowired
    private URLFilterInvocationSecurityMetadataSourceImpl urlMetadataSource;

    /**
     * 刷新权限
     * @return
     */
    @GetMapping(value = "/refresh")
    public Result refresh(){
        urlMetadataSource.loadResourceDefine();
        return new Result(ResultCode.SUCCESS,"刷新权限成功");
    }

}
