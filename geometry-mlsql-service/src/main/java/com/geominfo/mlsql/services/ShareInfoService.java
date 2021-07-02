package com.geominfo.mlsql.services;

import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.domain.po.ShareInfo;
import com.geominfo.mlsql.domain.result.SharedInfoResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: geometry-bi
 * @description: 分享数据层接口
 * @author: LF
 * @create: 2021/7/1 11:21
 * @version: 1.0.0
 */
public interface ShareInfoService {

    /***
     * @description: 新增脚本分享记录
     * @author: LF
     * @date: 2021/7/1
     * @param shareInfo
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo insertShareInfo(ShareInfo shareInfo);

    /**
     * @description: 查询给某个账户分享的所有未失效资源
     * @author: LF
     * @date: 2021/7/1
     * @param userId
     * @return java.util.List<com.geominfo.mlsql.domain.result.SharedInfoResult>
     */
    List<SharedInfoResult> getShareScriptsBySharedId(BigDecimal userId);


}
