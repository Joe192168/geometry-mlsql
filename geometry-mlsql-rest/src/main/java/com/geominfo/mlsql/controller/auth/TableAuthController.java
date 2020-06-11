package com.geominfo.mlsql.controller.auth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.domain.vo.MLSQLAuthTable;
import com.geominfo.mlsql.domain.vo.MLSQLTable;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.service.auth.TableAuthService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: TableAuthController
 * @author: anan
 * @create: 2020-06-10 11:47
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/auth")
@Api(value="权限接口类",tags={"权限接口"})
@Log4j2
public class TableAuthController {
    @Autowired
    private Message message ;

    @Autowired
    private TableAuthService tableAuthService;

    @RequestMapping("/tables")
    @ApiOperation(value = "获取用户表授权接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true)
    })
    public List<MLSQLAuthTable> getUserAuths(@RequestParam(value = "userName", required = true) String userName){
        List<MLSQLAuthTable> authTables = tableAuthService.fetchAuth(userName);
        return authTables;
    }

    @RequestMapping("/verify/tables")
    @ApiOperation(value = "验证表是否授权接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "MLSQLTable表json格式信息", name = "tables", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "owner", name = "owner", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "home", name = "home", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "auth_secret", name = "auth_secret", dataType = "String", paramType = "query", required = true)
    })
    public List<Boolean> getVerifyAuthTable(@ApiParam(value="tables", required = true) String tables,
                                       @ApiParam(value="owner", required = true) String owner,
                                       @ApiParam(value="home", required = true) String home,
                                       @ApiParam(value="auth_secret", required = false) String auth_secret){
        List<MLSQLAuthTable> authTablesInit = tableAuthService.fetchAuth(owner);
        Map<String, String> authTables = new HashMap<String, String>();
        List<Boolean> verifyAuth = new ArrayList<Boolean>();
        for(MLSQLAuthTable table : authTablesInit){
           String key =  table.getDb() + "_" + table.getTableName() + "_" + table.getTable_type() + "_" + table.getSource_type();
           String operateType = table.getOperate_type();
           authTables.put(key, operateType);
        }

        JSONArray tableJson = JSONObject.parseArray(tables);
        List<MLSQLTable> tableLists = tableJson.toJavaList(MLSQLTable.class);
        for(MLSQLTable mlsqlTable : tableLists){
            String db = tableAuthService.unifyColumn(mlsqlTable.getDb());
            String table = tableAuthService.unifyColumn(mlsqlTable.getTable());
            String tableType = tableAuthService.unifyColumn(((Map<String, String>)mlsqlTable.getTableType()).get("name"));
            String SourceType = tableAuthService.unifyColumn(mlsqlTable.getSourceType());
            String key = db + "_"  + table + "_"  + tableType+ "_"  + SourceType;
            boolean verify = tableAuthService.checkAuth(key, mlsqlTable, home, authTables);
            verifyAuth.add(verify);
        }
        return verifyAuth;
    }

}
