package com.geominfo.mlsql.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geominfo.mlsql.domain.po.ShareInfo;
import com.geominfo.mlsql.domain.result.SharedInfoResult;
import com.geominfo.mlsql.domain.vo.QueryShareInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: geometry-bi
 * @description: 分享数据库接口
 * @author: LF
 * @create: 2021/7/1 11:19
 * @version: 1.0.0
 */
@Mapper
public interface ShareInfoMapper extends BaseMapper<ShareInfo> {

    List<SharedInfoResult> getShareScriptsBySharedId(Integer userId);

    List<SharedInfoResult> getShareScriptsByUserIdAndTime(QueryShareInfoVo queryShareInfoVo);

}
