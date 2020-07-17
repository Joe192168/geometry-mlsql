package com.geominfo.mlsql.service.scriptfile.impl;

import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.domain.vo.ScriptUserRw;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.mapper.ScriptFileMapper;
import com.geominfo.mlsql.mapper.UserMapper;
import com.geominfo.mlsql.service.scriptfile.ScriptFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 获取脚本内容实现类
 * @author: BJZ
 * @create: 2020-06-04 14:55
 * @version: 3.0.0
 */
@Service
public class ScripteFileSverviceImpl implements ScriptFileService {


    @Autowired
    private ScriptFileMapper scriptFileMapper ;

    @Autowired
    private UserMapper userMapper;

    @Override
    public MlsqlScriptFile getScriptById(Integer id) {
        return scriptFileMapper.getScriptById(id);
    }

    @Override
    public String updateScriptFile(MlsqlScriptFile mlsqlScriptFile) {
        if((mlsqlScriptFile.getIsExpanded() == 0 )){
            mlsqlScriptFile.setIsExpanded(1);
        }
        scriptFileMapper.updateScriptFile(mlsqlScriptFile);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertScriptFile(MlsqlScriptFile mlsqlScriptFile, int userId) {
        mlsqlScriptFile.setIcon(mlsqlScriptFile.getIsDir() == mlsqlScriptFile.DIR ? "folder-close":"document");
        scriptFileMapper.insertScriptFile(mlsqlScriptFile);
        ScriptUserRw scriptUserRw = new ScriptUserRw();
        scriptUserRw.setFileId(mlsqlScriptFile.getId());
        scriptUserRw.setUserId(userId);
        scriptFileMapper.insertScriptUserRW(scriptUserRw);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String removeFile(int id, MlsqlUser mlsqlUser) {
        MlsqlScriptFile mlsqlScriptFile = scriptFileMapper.getScriptById(id);
        if(mlsqlScriptFile != null){
            if(mlsqlScriptFile.getIsDir() == MlsqlScriptFile.DIR){
                List<MlsqlScriptFile> files = scriptFileMapper.getScriptFileListByParentId(mlsqlScriptFile.getId());
                for (MlsqlScriptFile file : files) {
                    removeFile(file.getId(), mlsqlUser);
                }
                markDelete(mlsqlScriptFile.getScriptUserRws(), mlsqlUser);
            }else{
                markDelete(mlsqlScriptFile.getScriptUserRws(), mlsqlUser);
            }
        }
        return ReturnCode.SUCCESS;
    }

    @Override
    public String findScriptFileByPath(MlsqlUser mlsqlUser, String paths) {
        String[] pathArray = paths.split("\\.", paths.split("\\.").length-1);
        int parentId = -1;
        String content = ReturnCode.SCRIPT_FILE_NO_EXISTS;
        boolean flag = false;
        for(int i =0 ;i<pathArray.length;i++){
            Map<String, Object> map = new HashMap<>();
            map.put("userId", mlsqlUser.getId());
            map.put("path", pathArray[i]);
            if(i>0){
                map.put("parentId", parentId);
            }
            List<MlsqlScriptFile> fileList = scriptFileMapper.getScriptFileListByPathAndUser(map);
            parentId = -1;
            for(MlsqlScriptFile file : fileList){
                parentId = (int)file.getId();
                if(i<=pathArray.length){
                    if(file.getName().equals(pathArray[i]) && i==pathArray.length-1){
                        flag = true;
                        content = (String)file.getContent();
                        break;
                    }
                }
            }
            if(flag){
                break;
            }
        }
        return content;
    }

    @Override
    public List<MlsqlScriptFile> listScriptFileByUser(MlsqlUser mlsqlUser) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", mlsqlUser.getId());
        return scriptFileMapper.getScriptFileListByPathAndUser(map);
    }

    public void markDelete(List<ScriptUserRw> items, MlsqlUser mlsqlUser){
        items.stream().filter(
                (m) -> {
                    MlsqlUser temp_user = (MlsqlUser) m.getMlsqlUser();
                    return temp_user.getId() == mlsqlUser.getId();
                }).forEach((m) -> {
            m.setIsDelete(ScriptUserRw.Delete);
            scriptFileMapper.updateScriptUserRW(m);
        });
    }
}