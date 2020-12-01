package com.geominfo.mlsql.service.cluster;

import com.geominfo.mlsql.domain.vo.MlsqlApply;
import org.springframework.stereotype.Service;

/**
 * @program: geometry-mlsql
 * @description: ApplyService
 * @author: BJZ
 * @create: 2020-11-30 14:23
 * @version: 1.0.0
 */
@Service
public interface ApplyService {
    void insertApply(MlsqlApply mlsqlApply) ;
}