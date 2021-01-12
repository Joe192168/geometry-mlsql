package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.appruntimefull.WConnectTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: 集群后端配置Mpper
 * @author: ryan
 * @create: 2021-1-05 14:31
 * @version: 1.0.0
 */
@Mapper
@Component
public interface AppRuntimeMapper {

    void insertAppDS(WConnectTable wct);

    List<WConnectTable> getAppRuntimeList();

    void updateConnectParams(WConnectTable wct);

}
