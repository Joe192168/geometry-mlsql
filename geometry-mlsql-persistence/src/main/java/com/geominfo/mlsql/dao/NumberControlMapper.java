package com.geominfo.mlsql.dao;

import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

@Mapper
public interface NumberControlMapper {

    //查询maxmum
    BigDecimal selectMaxNum(String itemCode);

    //更新maxmum
    void updateMaxNum(String itemCode);

}
