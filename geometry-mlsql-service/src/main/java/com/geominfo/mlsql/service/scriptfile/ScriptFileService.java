package com.geominfo.mlsql.service.scriptfile;

import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.domain.vo.MlsqlUser;

import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: 获取脚本接口
 * @author: BJZ
 * @create: 2020-06-04 14:53
 * @version: 3.0.0
 */

public interface ScriptFileService {
    /**
      * description: by id get script
      * author: anan
      * date: 2020/7/17
      * param: id
      * return: MlsqlScriptFile
     */
    MlsqlScriptFile getScriptById(Integer id);

    /**
      * description: update script file
      * author: anan
      * date: 2020/7/17
      * param: MlsqlScriptFile
      * return: String
     */
    String updateScriptFile(MlsqlScriptFile mlsqlScriptFile);

    /**
      * description: insert script file
      * author: anan
      * date: 2020/7/17
      * param: MlsqlScriptFile
     *  param: userId
      * return: String
     */
    String insertScriptFile(MlsqlScriptFile mlsqlScriptFile, int userId);

    /**
      * description: update file status delete
      * author: anan
      * date: 2020/7/17
      * param: id
     *  param: mlsqlUser
      * return: String
     */
    String removeFile(int id, MlsqlUser mlsqlUser);

    /**
      * description: by path find file
      * author: anan
      * date: 2020/7/17
      * param: mlsqlUser
     *  param: path
      * return: String
     */
    String findScriptFileByPath(MlsqlUser mlsqlUser, String path);

    /**
      * description: get users file lists
      * author: anan
      * date: 2020/7/17
      * param: mlsqlUser
      * return: List<MlsqlScriptFile>
     */
    List<MlsqlScriptFile> listScriptFileByUser(MlsqlUser mlsqlUser);
}