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
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        List<SpaceMemberVo> spaceMemberVos = workSpaceMembersMapper.getSpaceMember(new BigDecimal(workSpaceInfoVo.getSpaceId()),workSpaceInfoVo.getSpaceMemberId());
        if (!CollectionUtils.isEmpty(spaceMemberVos)){
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(CommonConstants.PARAM_ERROR);
            return baseResultVo;
        }
        WorkSpaceMember workSpaceMember = new WorkSpaceMember();
        BigDecimal id = numberControlService.getMaxNum(SystemTableConstants.T_WORKSPACE_MEMBER_INFOS);
        workSpaceMember.setId(id);
        workSpaceMember.setSpaceId(new BigDecimal(workSpaceInfoVo.getSpaceId()));
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
        List<SpaceMemberVo> spaceMemberVoList = new ArrayList<>();
        List<User> userLists = FeignUtils.parseArray(authQueryApiService.getUsers(),User.class);
        List<Integer> spaceMemberIds = workSpaceMembersMapper.getSpaceMemberIdsBySpaceId(spaceId);
       //过滤已分配的人员
        for (Integer id :spaceMemberIds){
            for (int i = 0;i < userLists.size();i++) {
                if (id.equals(userLists.get(i).getId())) {
                    userLists.remove(i);
                }
            }
        }

        for (User user :userLists){
            SpaceMemberVo spaceMemberVo = new SpaceMemberVo();
            spaceMemberVo.setLoginName(user.getLoginName());
            spaceMemberVo.setUserName(user.getUserName());
            spaceMemberVo.setMemberId(BigDecimal.valueOf(user.getId()));
            spaceMemberVoList.add(spaceMemberVo);
        }
        return spaceMemberVoList;
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
