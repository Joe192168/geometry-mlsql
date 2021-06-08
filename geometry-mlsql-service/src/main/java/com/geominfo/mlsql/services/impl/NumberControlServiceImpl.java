package com.geominfo.mlsql.services.impl;

import com.geominfo.mlsql.dao.NumberControlMapper;
import com.geominfo.mlsql.services.NumberControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description: NumberControlServiceImpl
 * @author: LF
 * @create: 2019/11/22
 * @version: 1.0.0
 */
@Service
public class NumberControlServiceImpl implements NumberControlService {

    @Autowired
    private NumberControlMapper numberMapper;

    @Override
    @Transactional
    public BigDecimal getMaxNum(String itemCode) throws DataAccessException {
        //先更新序列号
        numberMapper.updateMaxmum(itemCode);
        //获取最新序列号
        BigDecimal num = numberMapper.selectMaxmum(itemCode);
        return num;
    }
}
