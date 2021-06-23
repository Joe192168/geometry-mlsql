package com.geominfo.mlsql.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geominfo.mlsql.domain.po.EngineInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/16 11:25
 * @version: 1.0.0
 */
@Mapper
public interface EngineInfoMapper extends BaseMapper<EngineInfo> {

    /**
     * @description: 查询所有的引擎信息
     * @author: LF
     * @date: 2021/6/16
     * @param
     * @return java.util.List<com.geominfo.mlsql.domain.po.EngineInfo>
     */
    List<EngineInfo> getEngineInfos();

    /**
     * @description: 根据引擎服务器查询引擎信息
     * @author: LF
     * @date: 2021/6/23
     * @param engineUri
     * @return java.util.List<com.geominfo.mlsql.domain.po.EngineInfo>
     */
    List<EngineInfo> getEngineInfoByEngineUri(String engineUri);

    /**
     * @description: 查询可配置的引擎
     * @author: LF
     * @date: 2021/6/17
     * @param spaceId
     * @return java.util.List<com.geominfo.mlsql.domain.po.EngineInfo>
     */
    List<EngineInfo> getEnginesBySpaceId(BigDecimal spaceId);

    /**
     * @description: 根据空间id查询引擎
     * @author: LF
     * @date: 2021/6/17
     * @param spaceId
     * @return java.util.List<com.geominfo.mlsql.domain.po.EngineInfo>
     */
    List<EngineInfo> getEngineLists(BigDecimal spaceId);
}
