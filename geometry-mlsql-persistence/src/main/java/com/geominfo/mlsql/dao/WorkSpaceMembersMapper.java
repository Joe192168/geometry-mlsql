package com.geominfo.mlsql.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geominfo.mlsql.domain.po.WorkSpaceMember;
import com.geominfo.mlsql.domain.vo.QueryWorkSpaceVo;
import com.geominfo.mlsql.domain.vo.SpaceMemberVo;
import com.geominfo.mlsql.domain.vo.WorkSpaceInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface WorkSpaceMembersMapper extends BaseMapper<WorkSpaceMember> {

    /**
     * @description:查找用户的默认空间
     * @author: LF
     * @date: 2021/6/10
     * @param userId
     * @return java.util.List<com.geominfo.mlsql.domain.po.WorkSpaceMember>
     */
    List<WorkSpaceMember> getUserDefaultSpace(BigDecimal userId);

    /**
     * @description:  设置默认空间
     * @author: LF
     * @date: 2021/6/10
     * @param userId, spaceId
     * @return void
     */
    void setDefaultSpace(@Param("userId")BigDecimal userId,@Param("spaceId") BigDecimal spaceId);

    /**
     * @description:修改人员
     * @author: LF
     * @date: 2021/6/10
     * @param userId
     * @return void
     */
    void updateSpaceInfoByUserId(BigDecimal userId);

    /**
     * @description: 获取该空间所属者空间信息
     * @author: LF
     * @date: 2021/6/11
     * @param spaceId
     * @return com.geominfo.mlsql.domain.po.WorkSpaceMember
     */
    WorkSpaceMember getWorkSpaceBySpaceId(BigDecimal spaceId);

    /**
     * @description: 删除空间成员
     * @author: LF
     * @date: 2021/6/11
     * @param spaceId, userId
     * @return java.lang.Boolean
     */
    Boolean deleteSpaceMember(@Param("spaceId") BigDecimal spaceId,@Param("userId")BigDecimal userId);

    /**
     * @description: 根据工作空间id查询空间成员
     * @author: LF
     * @date: 2021/6/15
     * @param spaceId
     * @return java.util.List<com.geominfo.mlsql.domain.vo.SpaceMemberVo>
     */
    List<SpaceMemberVo> getSpaceMemberBySpaceId(BigDecimal spaceId);

    List<Integer> getSpaceMemberIdsBySpaceId(BigDecimal spaceId);

    /**
     * @description: 根据空间id和成员id，查询空间成员信息
     * @author: LF
     * @date: 2021/6/22
     * @param spaceId, MemberId
     * @return java.util.List<com.geominfo.mlsql.domain.vo.SpaceMemberVo>
     */
    List<SpaceMemberVo> getSpaceMember(@Param("spaceId") BigDecimal spaceId,@Param("memberId")BigDecimal memberId);

}
