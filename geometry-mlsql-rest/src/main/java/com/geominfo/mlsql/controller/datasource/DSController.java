package com.geominfo.mlsql.controller.datasource;

import com.geominfo.mlsql.domain.vo.JDBCD;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.service.datasource.DataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: DSController
 * @author: anan
 * @create: 2020-11-30 15:42
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/api_v1")
@Api(value="数据源维护接口类",tags={"数据源维护接口"})
@Log4j2
public class DSController {
    @Autowired
    DataSourceService dataSourceService;

    @RequestMapping(value = "/ds/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增engine", httpMethod = "POST")
    public Message addDs(){
        JDBCD jdbcd = new JDBCD();
        jdbcd.setDb("test");
        jdbcd.setUrl("jdbc:mysql://192.168.0.45:3306/test");
        jdbcd.setName("root");
        jdbcd.setPassword("mysql");
        jdbcd.setJType("mysql");
        //Set<String> tableNames = dataSourceService.getTables(jdbcd); //获取所有表名 对应接口 /api_v1/ds/mysql/dbs
        //Map<String, Object> tableNames = dataSourceService.testDataSource(jdbcd); //获得连接是否正常  对应接口：/api_v1/ds/add
        ResultSet rs = dataSourceService.getQuery(jdbcd,"select max(id),min(id) from awh_test2"); //查询获取表最大值最小值 /api_v1/ds/mysql/column
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
}
