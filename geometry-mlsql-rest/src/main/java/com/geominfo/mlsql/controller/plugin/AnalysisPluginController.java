package com.geominfo.mlsql.controller.plugin;

import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlAnalysisPlugin;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.service.plugin.MlsqlAnalysisPluginService;
import com.geominfo.mlsql.service.scriptfile.ScriptFileService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: 分析插件控制类
 * @author: ryan(丁帅波)
 * @create: 2020-11-10 09:45
 * @version: 1.0.0
 */
@Api(value = "分析工坊插件", tags = {"分析工坊插件"})
@RestController
@RequestMapping("/api_v1/script_file")
@Log4j2
public class AnalysisPluginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScriptFileService scriptFileService;

    @Autowired
    private MlsqlAnalysisPluginService mlsqlAnalysisPluginService;


    @RequestMapping(value = "/plugin/publish", method = RequestMethod.POST)
    @ApiOperation(value = "插件发布", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "插件名", required = false, paramType = "query", dataType = "String")
    })
    public Message publishAsPlugin(@RequestParam(value = "id", required = true) Integer id,
                                   @RequestParam(value = "name", required = false) String name) {
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", mlsqlUser.getId());
        MlsqlScriptFile mlsqlScriptFile = scriptFileService.listScriptFileByUser(map);
        if (mlsqlScriptFile != null) {
            String analysisName;
            if (StringUtils.isBlank(name)) {
                analysisName = mlsqlScriptFile.getName();
            } else {
                analysisName = name;
            }
            map.put("name", analysisName);
            MlsqlAnalysisPlugin mlsqlAnalysisPlugin = mlsqlAnalysisPluginService.getMlsqlAnalysisList(map);
            String result;
            if (mlsqlAnalysisPlugin != null) {
                HashMap<String, Object> pluginMap = new HashMap<>();
                pluginMap.put("id", mlsqlAnalysisPlugin.getId());
                pluginMap.put("content", mlsqlScriptFile.getContent());
                result = mlsqlAnalysisPluginService.updateMlsqlAnalysis(pluginMap);
                return result.equals(InterfaceReturnInformation.SUCCESS) ? success(ReturnCode.RETURN_SUCCESS_STATUS,"update success") :
                        error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "update failed");
            } else {
                map.put("content", mlsqlScriptFile.getContent());
                map.put("mlsqlUserId", mlsqlUser.getId());
                result = mlsqlAnalysisPluginService.addMlsqlAnalysis(map);
                return result.equals(InterfaceReturnInformation.SUCCESS) ? success(ReturnCode.RETURN_SUCCESS_STATUS, "add success") :
                        error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "add failed");
            }
        }
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"null value");
    }


    @ApiOperation(value = "查询所有插件信息", httpMethod = "GET")
    @RequestMapping(value = "/plugins", method = RequestMethod.GET)
    public Message plugins(){
        List<MlsqlAnalysisPlugin> mlsqlAnalysisList = mlsqlAnalysisPluginService.getMlsqlAnalysisList();
        return mlsqlAnalysisList.size() > 0 ? success(ReturnCode.RETURN_SUCCESS_STATUS,"get plugins success")
                .addData("data",mlsqlAnalysisList) : success(ReturnCode.RETURN_SUCCESS_STATUS,"is empty").addData("data","");
    }


    @RequestMapping(value = "/plugin/get", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有插件信息", httpMethod = "GET")
    @ApiImplicitParam(name = "name", value = "插件名", required = true, paramType = "query", dataType = "String")
    public Message pluginGet(@RequestParam(value = "name",required = true) String name){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        MlsqlAnalysisPlugin mlsqlAnalysisList = mlsqlAnalysisPluginService.getMlsqlAnalysisList(map);

        return mlsqlAnalysisList != null ? success(ReturnCode.RETURN_SUCCESS_STATUS,"get pluginName success")
                .addData("data",mlsqlAnalysisList) : error(HttpStatus.SC_NOT_FOUND,"not found").addData("data","");
    }

}
