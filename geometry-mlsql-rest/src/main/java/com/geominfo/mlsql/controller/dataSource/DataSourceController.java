package com.geominfo.mlsql.controller.dataSource;

import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.dto.DataBaseDTO;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.services.DataSourceService;
import com.geominfo.mlsql.services.TSystemResourceService;
import com.geominfo.mlsql.utils.DtaBaseUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @title: DataSourceController
 * @desc: 数据源接口
 * @date 2021/6/7 16:00
 */
@RequestMapping("/dataSource")
@RestController
@Api(value = "数据源接口",tags = {"数据源接口"})
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private TSystemResourceService systemResourceService;

    @ApiOperation(value = "新增数据源", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceId", value = "当前用户对应的工作空间id", dataType = "int", paramType = "path",required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "path",required = true),
            @ApiImplicitParam(name = "dataBaseDTO", value = "数据库连接传输对象", paramType = "body",required = true)
    })
    @PostMapping("/addDataSource/{resourceId}/{userId}")
    public Message addDataSource(@RequestBody DataBaseDTO dataBaseDTO, @PathVariable BigDecimal resourceId,
                                 @PathVariable BigDecimal userId){
        try{
            Map<String,Object> map = dataSourceService.addDataSource(dataBaseDTO,resourceId,userId);
            if((Boolean) map.get("flag")){
                //新增数据源成功，记录接口操作日志
                return new Message().ok("数据源新增成功");
            }
            return new Message().error(map.get("msg").toString());
        }catch (Exception e){
            e.printStackTrace();
            return new Message().error(e.getMessage());
        }

    }

    @ApiOperation(value = "修改数据源", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceId", value = "当前用户对应的工作空间id", dataType = "int", paramType = "path",required = true),
            @ApiImplicitParam(name = "parentId", value = "父节点id", dataType = "int", paramType = "path",required = true),
            @ApiImplicitParam(name = "dataBaseDTO", value = "数据库连接传输对象", paramType = "body",required = true)
    })
    @PutMapping("/updateDataSourceById/{parentId}/{resourceId}")
    public Message updateDataSource(@RequestBody @ApiParam(value="数据源对象",required = true) DataBaseDTO dataBaseDTO, @PathVariable BigDecimal resourceId,@PathVariable BigDecimal parentId){
        try{
            Map<String,Object> map = dataSourceService.updateDataSource(dataBaseDTO,resourceId,parentId);
            if((Boolean) map.get("flag")){
                //新增数据源成功，记录接口操作日志
                return new Message().ok("数据源修改成功");
            }
            return new Message().error(map.get("msg").toString());
        }catch (Exception e){
            e.printStackTrace();
            return new Message().error(e.getMessage());
        }

    }

    @ApiOperation(value = "删除数据源", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceId", value = "被删除资源id", dataType = "int", paramType = "path",required = true),
    })
    @DeleteMapping("/deleteDataSource/{resourceId}")
    public Message deleteDataSource(@PathVariable BigDecimal resourceId){
      boolean delete =   dataSourceService.deleteDataSource(resourceId);
      if (delete) {
          return new Message().ok("删除成功");
      }
      return new Message().error("删除失败");
    }

    @ApiOperation(value = "测试连接数据源", httpMethod = "POST")
    @PostMapping("/testConnect")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "baseDTO", value = "数据库连接传输对象", paramType = "body",required = true)
    )
    public Message testConnect(@RequestBody DataBaseDTO baseDTO){
        boolean b = DtaBaseUtil.tryLink(baseDTO);
        if (b) {
            return new Message().ok("连接成功");
        }
        return new Message().error("连接失败");
    }

    @ApiOperation(value = "搜索数据源", httpMethod = "GET")
    @GetMapping("/selectDataSource")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "dataSourceName", value = "数据源名字", type = "String" , paramType = "param",required = true)
    )
    public Message selectDataSource(@RequestParam String dataSourceName){
        List<TSystemResources> dataSourceList = dataSourceService.selectDataSources(dataSourceName);
        if (dataSourceList.isEmpty()) {
            return new Message().error("未查询到数据源");
        }
        return new Message().ok().addData("data",dataSourceList);
    }

    @ApiOperation(value = "获取数据源所有表", httpMethod = "POST")
    @PostMapping("/getTableInfo")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "dataBaseDTO", value = "数据库连接传输对象", paramType = "body",required = true)
    )
    public Message getTableInfo(@RequestBody DataBaseDTO dataBaseDTO){
        List<Map<String, Object>> maps = dataSourceService.listTablesByDataSource(dataBaseDTO);
        if (maps.isEmpty()) {
            return new Message().error("数据库无表结构");
        }
        return new Message().ok().addData("data",maps);
    }

    @ApiOperation(value = "获取表字段", httpMethod = "POST")
    @PostMapping("/getColumnsByTableName")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "dataBaseDTO", value = "数据库连接传输对象", paramType = "body",required = true)
    )
    public Message getColumnsByTableName(@RequestBody DataBaseDTO dataBaseDTO){
        List<Map<String, Object>> maps = dataSourceService.getColumnsByTableName(dataBaseDTO);
        if (maps.isEmpty()) {
            return new Message().error("数据表无字段信息");
        }
        return new Message().ok().addData("data",maps);
    }
}
