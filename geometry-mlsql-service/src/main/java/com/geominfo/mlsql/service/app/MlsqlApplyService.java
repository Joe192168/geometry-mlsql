package com.geominfo.mlsql.service.app;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 应用信息接口
 * @author: ryan
 * @create: 2020-11-19 15：52
 * @version: 1.0.0
 */
public interface MlsqlApplyService {
    List<com.geominfo.mlsql.domain.vo.MlsqlApply> getMlsqlApplyList(Map<String, Object> map);
}
