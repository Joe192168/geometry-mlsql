package com.geominfo.mlsql.services;  /**
 * @title: RecoverStationService
 * @projectName geometry-mlsql
 * @description: TODO
 * @author Lenovo
 * @date 2021/6/1511:01
 */

import com.geominfo.mlsql.domain.po.TSystemResources;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther zrd
 * @Date 2021-06-15 11:01 
 *
 */

public interface RecoverStationService {

    List<TSystemResources> getAllRecoverResources(BigDecimal workSpaceId, BigDecimal userId);

    boolean deleteResource(BigDecimal resourceId);

    boolean recoverResource(BigDecimal resourceId);

    boolean reChoosePatentDir(BigDecimal resourceId, BigDecimal parentId);

    boolean deleteAll(BigDecimal workSpaceId,BigDecimal userId);
}
