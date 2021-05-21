package com.geominfo.mlsql.controller.account;

import com.geominfo.mlsql.commons.Result;
import com.geominfo.mlsql.commons.ResultCode;
import com.geominfo.mlsql.services.impl.URLFilterInvocationSecurityMetadataSourceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="权限池操作接口",tags={"权限池操作接口"})
public class PermissionPoolController {

    @Autowired
    private URLFilterInvocationSecurityMetadataSourceImpl urlMetadataSource;

    /**
     * 刷新权限
     * @return
     */
    @GetMapping(value = "/dealFilterChain/reload")
    public Result refresh(){
        urlMetadataSource.loadResourceDefine();
        return new Result(ResultCode.SUCCESS,"刷新权限成功");
    }

}
