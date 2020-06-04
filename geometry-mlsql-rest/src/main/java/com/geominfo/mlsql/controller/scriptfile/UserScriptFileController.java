package com.geominfo.mlsql.controller.scriptfile;

import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.service.scriptfile.ScripteFileSvervice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: UserScriptFileController
 * @author: BJZ
 * @create: 2020-06-04 15:59
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/script_file")
@Api(value="MLSQL脚本类接口", tags={"MLSQL脚本类"})
public class UserScriptFileController {
    @Autowired
    private Message message ;

    @Autowired
    private ScripteFileSvervice scripteFileSvervice;

    @RequestMapping("/getcontent")
    @ApiOperation(value = "通过ID获取脚本内容", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(value = "脚本id", name = "id", dataType = "String", paramType = "query", required = true)
    })
    public Message getcontent(@RequestParam(value = "id", required = true) String id ){
        MlsqlScriptFile mlsqlScriptFile = scripteFileSvervice.getScriptById(Integer.valueOf(id) ) ;
        if(mlsqlScriptFile != null ){
            if(mlsqlScriptFile.getIs_dir() == 1){
                return message.ok(200,"id对应的是目录").addData("data", mlsqlScriptFile);
            }
            return message.ok(200,"脚本执行成功").addData("data", mlsqlScriptFile);
        }else
        {
            return message.error(400,"不存在id对应文件内容").addData("data", id);
        }
    }
}
