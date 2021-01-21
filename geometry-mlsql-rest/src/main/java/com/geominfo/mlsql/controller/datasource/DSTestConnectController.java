package com.geominfo.mlsql.controller.datasource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.appruntimefull.WConnectTable;
import com.geominfo.mlsql.domain.vo.JDBCD;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlDs;
import com.geominfo.mlsql.domain.vo.ScriptRun;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.appruntimefull.AppRuntimeDsService;
import com.geominfo.mlsql.service.cluster.DsService;
import com.geominfo.mlsql.service.datasource.DataSourceService;
import com.geominfo.mlsql.service.proxy.ProxyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: 测试数据源连接参数是否正确
 * @author: ryan(丁帅波)
 * @create: 2021/01/04 17：02
 * @version: 1.0.0
 */
@Api(value = "测试数据源连接", tags = {"测试数据源连接"})
@RestController
@RequestMapping(value = "/connect/ds")
@Log4j2
public class DSTestConnectController extends BaseController {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private DsService dsService;

    @Autowired
    private AppRuntimeDsService appRuntimeDsService;

    @Autowired
    private ProxyService proxyService;

    @Value("${my_url.url2}")
    private String myUrl;

    @RequestMapping(value = "/testConnect", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "测试数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jType", value = "连接数据库类型", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "format", value = "连接数据库格式", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "db", value = "数据库名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "user", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "host", value = "连接地址", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "port", value = "端口号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "别名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "family", value = "列族", required = false, paramType = "query", dataType = "String")
    })
    public Message connectDS(JDBCD jdbcd) {
        JDBCD connectParams = dataSourceService.JDBCDConnectParams(jdbcd);
        if (jdbcd.getJType().replaceAll("\\s*", "").toLowerCase().equals("jdbc") ||
                jdbcd.getFormat().toLowerCase().equals("jdbc")) {
            Map<String, Object> connectInfo = dataSourceService.testDataSource(connectParams);
            if (connectInfo.get("flag") == Boolean.FALSE) {
                return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, connectInfo.get("msg").toString());
            }
            //测试Hbase
        } else if (jdbcd.getJType().replaceAll("\\s*", "").toLowerCase().equals("hbase") ||
                jdbcd.getFormat().toLowerCase().equals("hbase")) {
            String connectSQL = appRuntimeDsService.jointConnectPrams(connectParams);
            ScriptRun scriptRun = new ScriptRun("false", userName, connectSQL, "true");
            String jsonString = JSON.toJSONString(scriptRun);
            //调用engine/run/script接口
            ResponseEntity<String> responseEntity = proxyService.postForEntity(myUrl + "/cluster/api_v1/run/script", jsonString, String.class);
            JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
            JSONObject meta = JSON.parseObject(jsonObject.getString("meta"));
            JSONObject data = JSON.parseObject(jsonObject.getString("data"));
            if (Integer.valueOf(meta.getString("code")) != 200) {
                return error(Integer.valueOf(meta.getString("code")), data.getString("data"));
            }
        }
        return success(HttpStatus.SC_OK, "sava success").addData("jdbcd", jdbcd);
    }

    @ApiOperation(value = "修改数据源")
    @RequestMapping(value = "/updataParams", method = {RequestMethod.PUT})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jType", value = "连接数据库类型", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "format", value = "连接数据库格式", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "db", value = "数据库名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "user", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "host", value = "连接地址", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "port", value = "端口号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "别名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "family", value = "列族", required = false, paramType = "query", dataType = "String")
    })
    public Message updateConnectParams(JDBCD jdbcd) {
        JDBCD connectParams = dataSourceService.JDBCDConnectParams(jdbcd);
        requestParams.put("url", connectParams.getUrl());
        requestParams.put("driver", connectParams.getDriver());
        requestParams.put("family", connectParams.getFamily());
        MlsqlDs mlsqlDs = new MlsqlDs();
        mlsqlDs.setFormat(connectParams.getFormat());
        mlsqlDs.setName(connectParams.getName());
        mlsqlDs.setParams(JSONObject.toJSONString(requestParams));
        //修改mlsql_ds表中的数据
        dsService.updateDs(mlsqlDs);
        //修改w_connect_table表中的数据
        WConnectTable wct = appRuntimeDsService.getWConnectTable(connectParams);
        appRuntimeDsService.updateConnectParams(wct);
        //拼接参数
        String connectSQL = appRuntimeDsService.jointConnectPrams(connectParams);
        ScriptRun scriptRun = new ScriptRun("true", userName, connectSQL, "true");
        String jsonString = JSON.toJSONString(scriptRun);
        //调用engine/run/script接口
        ResponseEntity<String> responseEntity = proxyService.postForEntity(myUrl + "/cluster/api_v1/run/script", jsonString, String.class);
        return success(HttpStatus.SC_OK, "update sucess");
    }

}
