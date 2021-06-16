package com.geominfo.mlsql.services.impl;

import com.geominfo.authing.common.constants.CommonConstants;
import com.geominfo.authing.common.constants.SystemTableConstants;
import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.dao.WorkSpaceMembersMapper;
import com.geominfo.mlsql.domain.po.WorkSpaceMember;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.vo.SpaceMemberVo;
import com.geominfo.mlsql.domain.vo.WorkSpaceInfoVo;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.AuthQueryApiService;
import com.geominfo.mlsql.services.NumberControlService;
import com.geominfo.mlsql.services.WorkSpaceMemberService;
import com.geominfo.mlsql.utils.FeignUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class WorkSpaceMemberServiceImpl implements WorkSpaceMemberService {

    @Autowired
    private NumberControlService numberControlService;
    @Autowired
    private WorkSpaceMembersMapper workSpaceMembersMapper;
    @Autowired
    private AuthQueryApiService authQueryApiService;

    @Override
    public BaseResultVo insertSpaceMember(WorkSpaceInfoVo workSpaceInfoVo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        WorkSpaceMember workSpaceMember = new WorkSpaceMember();
        BigDecimal id = numberControlService.getMaxNum(SystemTableConstants.T_WORKSPACE_MEMBER_INFOS);
        workSpaceMember.setId(id);
        workSpaceMember.setWorkSpaceId(new BigDecimal(workSpaceInfoVo.getSpaceId()));
        workSpaceMember.setSpaceMemberId(workSpaceInfoVo.getSpaceMemberId());
        workSpaceMember.setSpaceOwnerId(workSpaceInfoVo.getSpaceOwnerId());
        workSpaceMember.setCreateTime(new Date());
        if (StringUtils.isBlank(workSpaceInfoVo.getState()))
            //默认为普通空间
            workSpaceMember.setState(CommonConstants.DEFAULT_STR_VAL);
        else
            workSpaceMember.setState(workSpaceInfoVo.getState());
        int i = workSpaceMembersMapper.insert(workSpaceMember);
        if (i>0){
            baseResultVo.setSuccess(Boolean.TRUE);
            baseResultVo.setReturnMsg(InterfaceMsg.INSERT_SPACE_MEMBER_SUCCESS.getMsg());
            return baseResultVo;
        }else {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(InterfaceMsg.INSERT_SPACE_MEMBER_FAIL.getMsg());
            return baseResultVo;
        }
    }

    @Override
    public Boolean updateSpaceMemberById(WorkSpaceMember workSpaceMember) {
        int i = workSpaceMembersMapper.updateById(workSpaceMember);
        if (i > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean deleteSpaceMember(BigDecimal spaceId,BigDecimal userId) {
        return workSpaceMembersMapper.deleteSpaceMember(spaceId,userId);
    }

    @Override
    public List<WorkSpaceMember> getUserDefaultSpace(BigDecimal userId) {
        return workSpaceMembersMapper.getUserDefaultSpace(userId);
    }

    @Override
    public void updateSpaceInfoByUserId(BigDecimal userId) {
        workSpaceMembersMapper.updateSpaceInfoByUserId(userId);
    }

    @Override
    public void setDefaultSpace(BigDecimal userId, BigDecimal spaceId) {
        workSpaceMembersMapper.setDefaultSpace(userId,spaceId);
    }

    @Override
    public WorkSpaceMember getWorkSpaceBySpaceId(BigDecimal spaceId) {
        return workSpaceMembersMapper.getWorkSpaceBySpaceId(spaceId);
    }

    @Override
    public List<SpaceMemberVo> getSpaceMemberBySpaceId(BigDecimal spaceId) {
        List<SpaceMemberVo> spaceMemberVos = workSpaceMembersMapper.getSpaceMemberBySpaceId(spaceId);
        for (SpaceMemberVo vo : spaceMemberVos){
            getSpaceMemberInfo(vo);
        }
        return spaceMemberVos;
    }

    @Override
    public List<SpaceMemberVo> getTransferMemberBySpaceId(BigDecimal spaceId) {
        List<SpaceMemberVo> spaceMemberVos = workSpaceMembersMapper.getSpaceMemberBySpaceId(spaceId);
        for (SpaceMemberVo vo : spaceMemberVos){
            //去掉工作原空间所有者
            if (vo.getOwnerId().equals(vo.getSpaceId())){
                continue;
            }
            getSpaceMemberInfo(vo);
        }
        return spaceMemberVos;
    }

    private void getSpaceMemberInfo(SpaceMemberVo vo){
        BigDecimal ownerId = vo.getMemberId();
        User user = FeignUtils.parseObject(authQueryApiService.getUserById(ownerId), User.class);
        if (user != null) {
           if (StringUtils.isNotBlank(user.getLoginName())){
               vo.setLoginName(user.getLoginName());
               vo.setUserName(user.getUserName());
           }else {
               vo.setLoginName(user.getUserName());
               vo.setUserName(user.getUserName());
           }
        }else {
            throw new RuntimeException(CommonConstants.AUTH_ERROR);
        }
    }

}
