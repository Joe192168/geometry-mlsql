package com.geominfo.mlsql.service.plugin.impl;

import com.geominfo.mlsql.domain.vo.MlsqlAnalysisPlugin;
import com.geominfo.mlsql.mapper.PluginMapper;
import com.geominfo.mlsql.service.plugin.MlsqlAnalysisPluginService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 分析插件接口实现
 * @author: ryan
 * @create: 2020-11-20 10:38
 * @version: 1.0.0
 */
@Service
public class MlsqlAnalysisPluginServiceImpl implements MlsqlAnalysisPluginService {

    @Autowired
    private PluginMapper pluginMapper;

    @Override
    public MlsqlAnalysisPlugin getMlsqlAnalysisList(Map<String, Object> map) {
        List<MlsqlAnalysisPlugin> mlsqlAnalysisPluginList = pluginMapper.getMlsqlAnalysisPluginList(map);
        MlsqlAnalysisPlugin mlsqlAnalysisPlugin = null;
        if (mlsqlAnalysisPluginList.size()>0){
           mlsqlAnalysisPlugin =  mlsqlAnalysisPluginList.get(0);
        }
        return mlsqlAnalysisPlugin;
    }

    @Override
    public List<MlsqlAnalysisPlugin> getMlsqlAnalysisList() {
        return pluginMapper.getMlsqlAnalysisPluginList(new HashMap<String,Object>());
    }

    @Override
    public String updateMlsqlAnalysis(Map<String, Object> map) {
        int result = pluginMapper.updateMlsqlAnalysis(map);
        return result > 0 ? InterfaceReturnInformation.SUCCESS : InterfaceReturnInformation.FAILED;
    }

    @Override
    public String addMlsqlAnalysis(Map<String, Object> map) {
        int result = pluginMapper.addMlsqlAnalysis(map);
        return result > 0 ? InterfaceReturnInformation.SUCCESS : InterfaceReturnInformation.FAILED;
    }
}
