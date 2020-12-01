package com.geominfo.mlsql.service.cluster;

import com.geominfo.mlsql.domain.vo.MlsqlDs;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: DsService
 * @author: BJZ
 * @create: 2020-11-26 16:06
 * @version: 1.0.0
 */
@Service
public interface DsService {
    void saveDs(MlsqlDs mlsqlDs) ;
    void deleteDs(MlsqlUser mlsqlUser , int pId) ;
    List<MlsqlDs> listDs(MlsqlUser mlsqlUser) ;
    List<MlsqlDs> getDs(MlsqlUser mlsqlUser ,String name ,String format) ;

    String getConnect(String name ,MlsqlUser mlsqlUser);

}