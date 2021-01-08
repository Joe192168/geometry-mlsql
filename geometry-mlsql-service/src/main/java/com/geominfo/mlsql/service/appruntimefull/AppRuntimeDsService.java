package com.geominfo.mlsql.service.appruntimefull;

import com.geominfo.mlsql.domain.appruntimefull.wConnectTable;
import com.geominfo.mlsql.domain.vo.JDBCD;

import java.util.List;

/**
 *
 * @program: geometry-mlsql
 * @description: mlsql启动数据源信息接口
 * @author: ryan
 * @create: 2021-1-05 15:01
 * @version: 1.0.0
 */
public interface AppRuntimeDsService {
    /**
     * 向表中插入启动数据
     * @param wct
     */
    void insertAppDS(wConnectTable wct);

    /**
     * 获取所有启动参数
     * @return List<wConnectTable>
     */
    List<wConnectTable> getAppRuntimeList();

    /**
     * 获取wConnectTable对象
     * @param jdbcd
     * @return wConnectTable
     */
    wConnectTable getWConnectTable(JDBCD jdbcd);

    /**
     * 拼接connect语句参数
     * @param connectParams
     * @return String
     */
    String jointConnectPrams(JDBCD connectParams);


    void updateConnectParams(wConnectTable wct);
}
