package com.geominfo.mlsql.service.analysisWork.impl;

import com.geominfo.mlsql.domain.vo.MlsqlWorkshopTable;
import com.geominfo.mlsql.mapper.WorkshopTableMapper;
import com.geominfo.mlsql.service.analysisWork.MlsqlWorkshopTableService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 分析工作信息接口实现
 * @author: ryan
 * @create: 2020-11-16 11：11
 * @version: 1.0.0
 */
@Service
public class MlsqlWorkshopTableServiceImpl implements MlsqlWorkshopTableService {


    public static final Integer RUNNING = 1;
    public static final Integer SUCCESS = 2;
    public static final Integer FAIL = 3;
    public static final Integer KILLED = 4;
    public static final Integer VIEW = 5;


    @Autowired
    private WorkshopTableMapper mlsqlWorkshopTableMapper;

    @Override
    public List<MlsqlWorkshopTable> getMlsqlWorkshopList(String sessionId, Integer mlsqlUserId) {
        return mlsqlWorkshopTableMapper.getMlsqlWorkshopList(sessionId, mlsqlUserId);
    }

    @Override
    public String mlsqlWorkshopSave(MlsqlWorkshopTable mlsqlWorkshopTable) {
        int result = mlsqlWorkshopTableMapper.mlsqlWorkshopSave(mlsqlWorkshopTable);
        return result > 0 ? InterfaceReturnInformation.SUCCESS : InterfaceReturnInformation.FAILED;
    }

    @Override
    public String mlsqlWorkshopDelete(String tableName) {
        int result = mlsqlWorkshopTableMapper.mlsqlWorkshopDelete(tableName);
        return result > 0 ? InterfaceReturnInformation.SUCCESS : InterfaceReturnInformation.FAILED;
    }

    @Override
    public MlsqlWorkshopTable getMlsqlWorkshop(Map<String, Object> map) {
        return mlsqlWorkshopTableMapper.getMlsqlWorkshop(map);
    }

    @Override
    public String updateMlsqlWorkshop(Map<String, Object> map) {
        int result = mlsqlWorkshopTableMapper.updateMlsqlWorkshop(map);
        return result > 0 ? InterfaceReturnInformation.SUCCESS : InterfaceReturnInformation.FAILED;
    }
}
