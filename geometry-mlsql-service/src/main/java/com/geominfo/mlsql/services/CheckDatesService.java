package com.geominfo.mlsql.services;

import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.vo.CheckParamVo;

/**
 * @program: geometry-bi
 * @description: 参数校验
 * @author: LF
 * @create: 2021/6/8 11:14
 * @version: 1.0.0
 */
public interface CheckDatesService {

    /**
     * @description: 校验资源参数接口
     * @author: LF
     * @date: 2021/6/8
     * @param resourceVo
     * @return java.lang.Boolean
     */
    Boolean CheckResourceDate(TSystemResources resourceVo);
}
