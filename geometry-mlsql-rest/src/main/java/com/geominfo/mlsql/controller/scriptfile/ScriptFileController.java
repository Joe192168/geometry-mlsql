package com.geominfo.mlsql.controller.scriptfile;

import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.service.scriptfile.ScriptFileService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @program: MLSQL CONSOLE后端接口
 * @description: UserScriptFileController
 * @author: BJZ
 * @create: 2020-06-04 15:59
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/api_v1")
@Api(value = "脚本维护类接口", tags = {"脚本维护类接口"})
@Log4j2
public class ScriptFileController extends BaseController {
    @Autowired
    private ScriptFileService scripteFileSvervice;

    @Autowired
    private UserService userService;


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

    @RequestMapping("/script_file/get")
    @ApiOperation(value = "通过ID获取脚本", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(value = "脚本id", name = "id", dataType = "String", paramType = "query", required = true)
    })
    public Message getcontent(@RequestParam(value = "id", required = true) String id) {
        MlsqlScriptFile mlsqlScriptFile = scripteFileSvervice.getScriptById(Integer.valueOf(id));
        if (!ObjectUtils.isEmpty(mlsqlScriptFile)) {
            if (mlsqlScriptFile.getIsDir() == 1) {
                return success(200, "id is catalog").addData("data", mlsqlScriptFile);
            }
            return success(200, "get content success").addData("data", mlsqlScriptFile.getContent());
        } else {
            return error(400, "id not exists").addData("data", id);
        }
    }

    @RequestMapping("/script_file")
    @ApiOperation(value = "保存脚本", httpMethod = "POST")
    public Message scriptFile(@RequestBody MlsqlScriptFile mlsqlScriptFile){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        if(mlsqlUser.getStatus().equals(MlsqlUser.STATUS_PAUSE)){
            return error(ReturnCode.RETURN_ERROR_STATUS, "you can not operate because this account have be set pause");
        }
        String msg = "";
        if(mlsqlScriptFile.getId() > 0 ){
            msg = scripteFileSvervice.updateScriptFile(mlsqlScriptFile);
        }else{
            msg = scripteFileSvervice.insertScriptFile(mlsqlScriptFile,mlsqlUser.getId());
        }
        return msg.equals(InterfaceReturnInformation.SUCCESS)?success(ReturnCode.RETURN_SUCCESS_STATUS, "save opeator sucess")
                :error(ReturnCode.RETURN_ERROR_STATUS, "save opeator faild");
    }

    @RequestMapping("/script_file/remove")
    @ApiOperation(value = "删除脚本或目录", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(value = "脚本或目录id", name = "id", dataType = "int", paramType = "query", required = true)
    })
    public Message removeScriptFile(@RequestParam(value = "id", required = true) int id){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        if(mlsqlUser.getStatus() !=null && mlsqlUser.getStatus().equals(MlsqlUser.STATUS_PAUSE)){
            return error(ReturnCode.RETURN_ERROR_STATUS, "you can not operate because this account have be set pause");
        }
        String msg = scripteFileSvervice.removeFile(id, mlsqlUser);
        return msg.equals(InterfaceReturnInformation.SUCCESS)?success(ReturnCode.RETURN_SUCCESS_STATUS, "delete opeator sucess")
                :error(ReturnCode.RETURN_ERROR_STATUS, "delete opeator faild");
    }

    @RequestMapping("/getFileByUser")
    @ApiOperation(value = "获取用户脚本列表", httpMethod = "GET")
    public Message listScriptFile(){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        List<MlsqlScriptFile> mlsqlScriptFileList = scripteFileSvervice.listScriptFileByUser(mlsqlUser);
        if(mlsqlScriptFileList == null || mlsqlScriptFileList.size() == 0){
            MlsqlScriptFile mlsqlScriptFile = new MlsqlScriptFile();
            mlsqlScriptFile.setName("MLSQL_SCRIPT_CENTOR");
            mlsqlScriptFile.setIsDir(MlsqlScriptFile.DIR);
            mlsqlScriptFile.setContent(null);
            mlsqlScriptFile.setParentId(-1);
            scripteFileSvervice.insertScriptFile(mlsqlScriptFile, mlsqlUser.getId());
        }
        return success(200, "get script file list").addData("data", mlsqlScriptFileList);
    }

    @RequestMapping("/script_file/include")
    @ApiOperation(value = "获取脚本内容", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(value = "owner", name = "owner", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "path", name = "path", dataType = "String", paramType = "query", required = true)
    })
    public Message includeScriptFile(@RequestParam(value = "owner", required = true) String owner,
                                     @RequestParam(value = "path", required = true) String path){
        MlsqlUser mlsqlUser = userService.getUserByName(owner);
        if(mlsqlUser == null){
            return error(ReturnCode.RETURN_ERROR_STATUS,"user:" + owner + "is not exists");
        }
        String msg = scripteFileSvervice.findScriptFileByPath(mlsqlUser, path);
        return msg.equals(InterfaceReturnInformation.SCRIPT_FILE_NO_EXISTS) == false?success(ReturnCode.RETURN_SUCCESS_STATUS, "get content by path sucess").addData("data", msg)
                :error(ReturnCode.RETURN_ERROR_STATUS, "get content by path  faild").addData("data", msg);
    }

}
