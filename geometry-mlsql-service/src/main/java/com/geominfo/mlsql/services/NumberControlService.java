package com.geominfo.mlsql.services;

import java.math.BigDecimal;

public interface NumberControlService {

    /*
    *@Description 根据表名获取该表最大的ID
    *@Author LF
    *@Date  2019/11/22 
    *@Param [itemCode]
    *@return java.math.BigDecimal
    **/  
    BigDecimal getMaxmum(String tableName);
}
