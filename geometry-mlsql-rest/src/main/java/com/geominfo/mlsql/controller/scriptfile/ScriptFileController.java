package com.geominfo.mlsql.controller.scriptfile;

import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.service.scriptfile.ScriptFileService;
import com.geominfo.mlsql.service.scriptfile.impl.ScripteFileSverviceImpl;
import com.sun.istack.internal.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.ObjectUtils;
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
@RequestMapping("/scriptfile")
@Api(value = "MLSQL脚本类接口", tags = {"MLSQL脚本类"})
@Log4j2
public class ScriptFileController {

    @Autowired
    private Message message;

    @Autowired
    private ScriptFileService scripteFileSvervice;


    /**
      * @description: 通过ID获取脚本内容
      *
      * @author: BJZ
      *
      * @date: 2020/6/11 0011
      *
      * @param: 脚本ID
      *
      * @return:  脚本内容
     */
    @RequestMapping("/getcontent")
    @ApiOperation(value = "通过ID获取脚本内容", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(value = "脚本id", name = "id", dataType = "String", paramType = "query", required = true)
    })
    public Message getcontent(@RequestParam(value = "id", required = true)@NotNull String id) {
        MlsqlScriptFile mlsqlScriptFile = scripteFileSvervice.getScriptById(Integer.valueOf(id));
        if (!ObjectUtils.isEmpty(mlsqlScriptFile)) {
            if (mlsqlScriptFile.getIs_dir() == 1) {
                return message.ok(200, "id is catalog").addData("data", mlsqlScriptFile);
            }
            return message.ok(200, "get content success").addData("data", mlsqlScriptFile);
        } else {
            return message.error(400, "id not exists").addData("data", id);
        }
    }


}
