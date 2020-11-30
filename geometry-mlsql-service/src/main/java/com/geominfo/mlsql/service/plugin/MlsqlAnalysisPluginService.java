package com.geominfo.mlsql.service.plugin;

import com.geominfo.mlsql.domain.vo.MlsqlAnalysisPlugin;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 分析插件接口
 * @author: ryan
 * @create: 2020-11-20 10:37
 * @version: 1.0.0
 */
public interface MlsqlAnalysisPluginService {

    /**
     * description: 查询插件表中所有信息
     * author: ryan
     * date: 2020/11/20
     * param: map
     * return: MlsqlAnalysisPlugin
     */
    MlsqlAnalysisPlugin getMlsqlAnalysisList(Map<String, Object> map);


    /**
     * description:
     * author: ryan
     * date: 2020/11/20
     * param: map
     * return: List<MlsqlAnalysisPlugin>
     */
    List<MlsqlAnalysisPlugin> getMlsqlAnalysisList();


    /**
     * description: 修改内容
     * author: ryan
     * date: 2020/11/20
     * param: map
     * return: success & failed
     */
    String updateMlsqlAnalysis(Map<String, Object> map);

    /**
     * description: 向插件表中插入信息
     * author: ryan
     * date: 2020/11/20
     * param: map
     * return: success & failed
     */
    String addMlsqlAnalysis(Map<String, Object> map);
}
