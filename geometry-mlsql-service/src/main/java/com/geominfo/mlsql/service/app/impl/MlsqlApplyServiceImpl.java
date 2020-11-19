package com.geominfo.mlsql.service.app.impl;

import com.geominfo.mlsql.domain.vo.MlsqlApply;
import com.geominfo.mlsql.mapper.AppMapper;
import com.geominfo.mlsql.service.app.MlsqlApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 应用信息接口实现
 * @author: ryan
 * @create: 2020-11-19 16：17
 * @version: 1.0.0
 */
@Service
public class MlsqlApplyServiceImpl implements MlsqlApplyService {

    @Autowired
    private AppMapper appMapper;

    @Override
    public List<MlsqlApply> getMlsqlApplyList(Map<String, Object> map) {
        return appMapper.getMlsqlApplyList(map);
    }
}
