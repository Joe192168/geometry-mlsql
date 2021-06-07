package com.geominfo.mlsql.dao;

import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

@Mapper
public interface NumberControlMapper {

    //查询maxmum
    BigDecimal selectMaxmum(String itemCode);

    //更新maxmum
    int updateMaxmum(String itemCode);

}
