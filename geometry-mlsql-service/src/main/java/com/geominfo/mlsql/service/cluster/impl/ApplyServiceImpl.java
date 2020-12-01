package com.geominfo.mlsql.service.cluster.impl;

import com.geominfo.mlsql.domain.vo.MlsqlApply;
import com.geominfo.mlsql.mapper.MlsqlApplyMapper;
import com.geominfo.mlsql.service.cluster.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: geometry-mlsql
 * @description: ApplyServiceImpl
 * @author: BJZ
 * @create: 2020-11-30 14:24
 * @version: 1.0.0
 */
@Service
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    private MlsqlApplyMapper mlsqlApplyMapper ;

    @Override
    public void insertApply(MlsqlApply mlsqlApply) {
        mlsqlApplyMapper.insertApply(mlsqlApply);
    }
}