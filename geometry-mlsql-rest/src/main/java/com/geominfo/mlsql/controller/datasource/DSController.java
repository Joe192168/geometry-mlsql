package com.geominfo.mlsql.controller.datasource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.*;
import com.geominfo.mlsql.service.cluster.DsService;
import com.geominfo.mlsql.service.datasource.DataSourceService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.util.ExtractClassMsgUtil;
import com.geominfo.mlsql.utils.JSONTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: DSController
 * @author: anan
 * @create: 2020-11-30 15:42
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/api_v1/ds")
@Api(value="数据源维护接口类",tags={"数据源维护接口"})
@Log4j2
public class DSController extends BaseController {
    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private DsService dsService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/ad", method = RequestMethod.GET)
    @ApiOperation(value = "新增engine", httpMethod = "POST")
    public Message addD(){
        JDBCD jdbcd = new JDBCD();
        jdbcd.setDb("test");
        jdbcd.setUrl("jdbc:mysql://192.168.0.47:3306/test");
        jdbcd.setName("root");
        jdbcd.setPassword("123456");
        jdbcd.setJType("mysql");
        Set<String> tableNames = dataSourceService.getTables(jdbcd); //获取所有表名 对应接口 /api_v1/ds/mysql/dbs
        //Map<String, Object> tableNames = dataSourceService.testDataSource(jdbcd); //获得连接是否正常  对应接口：/api_v1/ds/add
        ResultSet rs = dataSourceService.getQuery(jdbcd,"select max(id),min(id) from test_info"); //查询获取表最大值最小值 /api_v1/ds/mysql/column
        try {
            while(rs.next()){
                int max = rs.getInt(1);
                String min = rs.getString(2);
                System.out.println("max:"+max+" min："+min);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "增加数据源",tags = {"增加数据源"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jType",value = "连接数据库类型",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "format",value = "连接数据库格式",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "db",value = "数据库名",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "user",value = "用户名",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password",value = "密码",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "host",value = "连接地址",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "port",value = "端口号",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name",value = "名称",required = false, paramType = "query", dataType = "String")
    })
    public Message addDs(JDBCD jdbcd){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        if (jdbcd.getJType().equals("mysql") && jdbcd.getFormat().equals("jdbc")){
            JDBCD connectParams = new JDBCD();
            connectParams.setDb(jdbcd.getDb());
            connectParams.setUrl("jdbc:mysql://" + jdbcd.getHost() +":"+ jdbcd.getPort()+"/"+jdbcd.getDb());
            connectParams.setName(jdbcd.getUser());
            connectParams.setPassword(jdbcd.getPassword());
            connectParams.setJType(jdbcd.getJType());

            requestParams.put("url",connectParams.getUrl());
            requestParams.put("driver","com.mysql.jdbc.Driver");
            Map<String, Object> connectInfo = dataSourceService.testDataSource(connectParams);
            if(connectInfo.get("flag") == Boolean.FALSE){
                return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,connectInfo.get("msg").toString());
            }
        }
        String nameAs = null;
        if (StringUtils.isBlank(jdbcd.getName())) {
            nameAs = jdbcd.getDb();
            requestParams.put("name",nameAs);
        }else {
            nameAs = jdbcd.getName();
        }
        dsService.saveDs(new MlsqlDs(nameAs,jdbcd.getFormat(), JSONObject.toJSONString(requestParams),mlsqlUser.getId()));
        return success(HttpStatus.SC_OK,"sava success");
    }

    @ApiOperation(value = "查询所有数据源",tags = {"查询所有数据源"})
    @RequestMapping(value = "/list", method = {RequestMethod.POST,RequestMethod.GET})
    public Message list(){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        List<MlsqlDs> mlsqlDs = dsService.listDs(mlsqlUser);
        List<String> list = ExtractClassMsgUtil.extractClassName(MlsqlDs.class);
        return success(HttpStatus.SC_OK,"success").addData("schema",list).addData("data",mlsqlDs);
    }

    @ApiOperation(value = "获取单个数据源",tags = {"获取单个数据源"})
    @RequestMapping(value = "/mysql/connect/get",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiImplicitParam(name = "name",value = "名称",required = true, paramType = "query", dataType = "String")
    public Message getMySQLConnect(@RequestParam(value = "name",required = true) String name){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        return success(HttpStatus.SC_OK,"get success").addData("connect",dsService.getConnect(name,mlsqlUser));
    }

    @ApiOperation(value = "删除单个数据源",tags = {"删除单个数据源"})
    @RequestMapping(value = "/remove", method = {RequestMethod.POST,RequestMethod.GET})
    public Message remove(@RequestParam(value = "id", required = true) Integer id){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        dsService.deleteDs(mlsqlUser,id);
        ExtractClassMsgUtil.extractClassName(MlsqlDs.class);
        return success(HttpStatus.SC_OK,"remove success");
    }

    @RequestMapping(value = "/mysql/column",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value = "查询表中最大最小值",tags = {"查询表中最大最小值"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cloumnName", value = "列名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dbName", value = "数据库名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tableName", value = "表名", required = true, paramType = "query", dataType = "String"),
    })
    public Message getCloumn(@RequestParam(value = "cloumnName",required = true) String cloumnName,
                             @RequestParam(value = "dbName", required = true) String dbName,
                             @RequestParam(value = "tableName",required = true) String tableName){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        Stream<MlsqlDs> jdbc = dsService.listDs(mlsqlUser).stream().filter(mlsqlDs -> mlsqlDs.getFormat().equals("jdbc"));
        ResultSet rs = jdbc.map(mlsqlDs -> JSON.parseObject(mlsqlDs.getParams(), JDBCD.class))
                .filter(jdbcd -> jdbcd.getJType().equals("mysql")).filter(jdbcd -> jdbcd.getDb().equals(dbName))
                .map(jdbcd -> dsService.showTable(jdbcd, cloumnName,tableName)).limit(1).collect(Collectors.toList()).get(0);
        Integer max = null;
        String min = null;
        try {
            while(rs.next()){
                max = rs.getInt(1);
                min = rs.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success(HttpStatus.SC_OK,"get success").addData("min",min).addData("max",max);
    }


    @RequestMapping(value = "/mysql/dbs", method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value = "获取库中所有表名", tags = {"获取库中所有表名"})
    public Message getDBs(){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        Stream<MlsqlDs> jdbc = dsService.listDs(mlsqlUser).stream().filter(mlsqlDs -> mlsqlDs.getFormat().equals("jdbc"));
        List<DSDB> list = jdbc.map(mlsqlDs -> JSON.parseObject(mlsqlDs.getParams(), JDBCD.class)).filter(jdbcd -> jdbcd.getJType().equals("mysql"))
                .map(jdbcd -> new DSDB(jdbcd.getName(), jdbcd.getDb(), dsService.showTable(jdbcd))).collect(Collectors.toList());
        List<String> entiyInfo = ExtractClassMsgUtil.extractClassName(DSDB.class);
        return success(HttpStatus.SC_OK,"get success").addData("schema",entiyInfo).addData("data",list);
    }
}
