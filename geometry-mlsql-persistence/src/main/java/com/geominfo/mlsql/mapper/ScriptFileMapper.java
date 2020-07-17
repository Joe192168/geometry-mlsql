package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.domain.vo.ScriptUserRw;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 脚本映射类
 * @author: BJZ
 * @create: 2020-06-04 14:48
 * @version: 3.0.0
 */
@Mapper
@Component
public interface ScriptFileMapper {

    MlsqlScriptFile getScriptById(Integer id);
    /**
      * description:
      * author: anan
      * date: 2020/7/17
      * param:
      * return:
     */
    int updateScriptFile(MlsqlScriptFile mlsqlScriptFile);
    /**
     * description:
     * author: anan
     * date: 2020/7/17
     * param:
     * return:
     */
    int insertScriptFile(MlsqlScriptFile mlsqlScriptFile);
    /**
     * description:
     * author: anan
     * date: 2020/7/17
     * param:
     * return:
     */
    int insertScriptUserRW(ScriptUserRw scriptUserRw);
    /**
     * description:
     * author: anan
     * date: 2020/7/17
     * param:
     * return:
     */
    List<ScriptUserRw> getScriptUserRW(Integer id);
    /**
     * description:
     * author: anan
     * date: 2020/7/17
     * param:
     * return:
     */
    int updateScriptUserRW(ScriptUserRw scriptUserRw);
    /**
     * description:
     * author: anan
     * date: 2020/7/17
     * param:
     * return:
     */
    List<MlsqlScriptFile> getScriptFileListByParentId(int parentId);
    /**
     * description:
     * author: anan
     * date: 2020/7/17
     * param:
     * return:
     */
    List<MlsqlScriptFile> getScriptFileListByPathAndUser(Map<String, Object> map);
}