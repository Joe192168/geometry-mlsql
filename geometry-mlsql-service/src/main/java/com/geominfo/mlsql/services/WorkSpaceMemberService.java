package com.geominfo.mlsql.services;

import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.domain.param.WorkSpaceMemberParam;
import com.geominfo.mlsql.domain.po.WorkSpaceMember;
import com.geominfo.mlsql.domain.result.SpaceMemberResult;
import com.geominfo.mlsql.domain.vo.SpaceMemberVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/9 11:19
 * @version: 1.0.0
 */
public interface WorkSpaceMemberService {

    /**
     * @description: 新增工作空间成员信息
     * @author: LF
     * @date: 2021/6/9
     * @param workSpaceMemberParam
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo insertSpaceMember(WorkSpaceMemberParam workSpaceMemberParam);

    /**
     * @description: 修改工作空间信息，根据ID修改
     * @author: LF
     * @date: 2021/6/11
     * @param workSpaceMember
     * @return java.lang.Boolean
     */
    Boolean updateSpaceMemberById(WorkSpaceMember workSpaceMember);

    /**
     * @description: 删除工作空间成员
     * @author: LF
     * @date: 2021/6/11
     * @param spaceId, userId
     * @return java.lang.Boolean
     */
    Boolean deleteSpaceMember(BigDecimal spaceId,BigDecimal userId);

    /**
     * @description: 查询用户的默认空间
     * @author: LF
     * @date: 2021/6/10
     * @param userId
     * @return java.util.List<com.geominfo.mlsql.domain.po.WorkSpaceMember>
     */
    List<WorkSpaceMember> getUserDefaultSpace(BigDecimal userId);

    /**
     * @description:修改用户所有空间为普通空间
     * @author: LF
     * @date: 2021/6/10
     * @param userId
     * @return void
     */
    void updateSpaceInfoByUserId(BigDecimal userId);

    /**
     * @description:设置用户默认空间
     * @author: LF
     * @date: 2021/6/10
     * @param userId, spaceId
     * @return void
     */
    void setDefaultSpace(BigDecimal userId,BigDecimal spaceId);

    /**
     * @description: 获取该空间所属者空间信息
     * @author: LF
     * @date: 2021/6/11
     * @param spaceId
     * @return com.geominfo.mlsql.domain.po.WorkSpaceMember
     */
    WorkSpaceMember getWorkSpaceBySpaceId(BigDecimal spaceId);

    /**
     * @description: 根据工作空间id查询空间成员
     * @author: LF
     * @date: 2021/6/15
     * @param spaceId
     * @return java.util.List<com.geominfo.mlsql.domain.result.SpaceMemberResult>
     */
    List<SpaceMemberResult> getSpaceMemberBySpaceId(BigDecimal spaceId);

    /**
     * @description: 可转让的空间成员
     * @author: LF
     * @date: 2021/6/15
     * @param spaceId
     * @return java.util.List<com.geominfo.mlsql.domain.vo.SpaceMemberResult>
     */
    List<SpaceMemberResult> getTransferMemberBySpaceId(BigDecimal spaceId);

}
