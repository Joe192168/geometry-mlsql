package com.geominfo.mlsql.services.impl;

import com.geominfo.authing.common.constants.CommonConstants;
import com.geominfo.authing.common.constants.ResourceTypeConstants;
import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.po.WorkSpaceMember;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.vo.QueryWorkSpaceVo;
import com.geominfo.mlsql.domain.vo.SystemResourceVo;
import com.geominfo.mlsql.domain.vo.WorkSpaceInfoVo;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.AuthQueryApiService;
import com.geominfo.mlsql.services.SystemResourceService;
import com.geominfo.mlsql.services.WorkSpaceManagerService;
import com.geominfo.mlsql.services.WorkSpaceMemberService;
import com.geominfo.mlsql.utils.FeignUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/9 15:46
 * @version: 1.0.0
 */
@Service
public class WorkSpaceManagerServiceImpl implements WorkSpaceManagerService {
    @Autowired
    private SystemResourceService systemResourceService;
    @Autowired
    private WorkSpaceMemberService workSpaceMemberService;
    @Autowired
    private TSystemResourcesDao systemResourcesDao;
    @Autowired
    private AuthQueryApiService authQueryApiService;

    @Override
    public BaseResultVo insertWorkSpace(WorkSpaceInfoVo workSpaceInfoVo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        TSystemResources systemResources = new TSystemResources();
        systemResources.setResourceName(workSpaceInfoVo.getWorkSpaceName());
        systemResources.setDisplayName(workSpaceInfoVo.getWorkSpaceName());
        systemResources.setResourceTypeId(new BigDecimal(ResourceTypeConstants.GZKJ_ML));
        systemResources.setOwner(workSpaceInfoVo.getSpaceOwnerId());
        systemResources.setContentInfo(workSpaceInfoVo.getDescribe());
        baseResultVo = systemResourceService.insertResource(systemResources);
        if (baseResultVo.isSuccess()) {
            baseResultVo = workSpaceMemberService.insertSpaceMember(workSpaceInfoVo);
            if (baseResultVo.isSuccess()) {
                return baseResultVo;
            } else {
                return baseResultVo;
            }
        } else {
            return baseResultVo;
        }
    }

    @Override
    public BaseResultVo updateWorkSpace(WorkSpaceInfoVo workSpaceInfoVo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        TSystemResources systemResources = new TSystemResources();
        if (StringUtils.isBlank(workSpaceInfoVo.getSpaceId())) {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(InterfaceMsg.PARAM_ERROR.getMsg());
            return baseResultVo;
        }
        systemResources.setId(new BigDecimal(workSpaceInfoVo.getSpaceId()));
        systemResources.setResourceTypeId(new BigDecimal(ResourceTypeConstants.GZKJ_ML));
        if (StringUtils.isNotBlank(workSpaceInfoVo.getWorkSpaceName()))
            systemResources.setResourceName(workSpaceInfoVo.getWorkSpaceName());
        if (StringUtils.isNotBlank(workSpaceInfoVo.getDescribe()))
            systemResources.setContentInfo(workSpaceInfoVo.getDescribe());
        return systemResourceService.updateResource(systemResources);

    }

    @Override
    public Boolean deleteWorkSpace(BigDecimal id) {
        return systemResourceService.deleteResourceById(id);
    }

    @Override
    public BaseResultVo setDefaultWorkSpace(BigDecimal spaceId, BigDecimal userId) {
        BaseResultVo baseResultVo = new BaseResultVo();
        //查询该用户下的默认空间
        List<WorkSpaceMember> defaultSpaces = workSpaceMemberService.getUserDefaultSpace(userId);
        //判断用户是否有默认空间
        if (!CollectionUtils.isEmpty(defaultSpaces)) {
            WorkSpaceMember defaultSpace = defaultSpaces.get(0);
            //默认空间不可重复设置
            if (!defaultSpace.getWorkSpaceId().equals(spaceId)) {
                //将原默认空间修改为普通空间
                workSpaceMemberService.updateSpaceInfoByUserId(userId);
                //设置默认空间
                workSpaceMemberService.setDefaultSpace(userId, spaceId);
                baseResultVo.setSuccess(Boolean.TRUE);
                baseResultVo.setReturnMsg(InterfaceMsg.SET_DEFAULT_SPACE_SUCCESS.getMsg());
                return baseResultVo;
            } else {
                baseResultVo.setSuccess(Boolean.FALSE);
                baseResultVo.setReturnMsg(InterfaceMsg.SET_DEFAULT_SPACE_FAIL.getMsg());
                return baseResultVo;
            }

        } else {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(InterfaceMsg.SET_DEFAULT_SPACE_FAIL.getMsg());
            return baseResultVo;
        }

    }

    @Override
    public BaseResultVo transferWorkSpace(BigDecimal spaceId, BigDecimal userId) {
        BaseResultVo baseResultVo = new BaseResultVo();
        WorkSpaceMember spaceInfo = workSpaceMemberService.getWorkSpaceBySpaceId(spaceId);
        if (!CommonConstants.DEFAULT_STR_VAL.equals(spaceInfo.getState())) {
            spaceInfo.setSpaceOwnerId(userId);
            spaceInfo.setSpaceMemberId(userId);
            Boolean flag = workSpaceMemberService.updateSpaceMemberById(spaceInfo);
            if (flag) {
                baseResultVo.setSuccess(Boolean.TRUE);
                baseResultVo.setReturnMsg(InterfaceMsg.TRANSFER_SPACE_SUCCESS.getMsg());
                return baseResultVo;
            } else {
                baseResultVo.setSuccess(Boolean.FALSE);
                baseResultVo.setReturnMsg(InterfaceMsg.TRANSFER_SPACE_FAIL.getMsg());
                return baseResultVo;
            }
        } else {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(InterfaceMsg.SPACE_NOT_TRANSFER.getMsg());
            return baseResultVo;
        }
    }

    @Override
    public List<WorkSpaceInfoVo> getWorkSpaceLists(BigDecimal userId) {
        List<WorkSpaceInfoVo> workSpaceInfoVos = systemResourcesDao.getWorkSpaceLists(userId);
        if (!CollectionUtils.isEmpty(workSpaceInfoVos)) {
            for (WorkSpaceInfoVo vo : workSpaceInfoVos) {
                getWorkSpaceInfo(vo);
            }
            return workSpaceInfoVos;
        } else {
            throw new RuntimeException(InterfaceMsg.QUERY_ERROR.getMsg());
        }
    }

    private void getWorkSpaceInfo(WorkSpaceInfoVo vo){

        BigDecimal ownerId = vo.getSpaceOwnerId();
        User user = FeignUtils.parseObject(authQueryApiService.getUserById(ownerId), User.class);
        if (user != null) {
            if (StringUtils.isNotBlank(user.getLoginName())) {
                vo.setOwnerName(user.getLoginName());
            }else {
                vo.setOwnerName(user.getUserName());
            }
        }else {
            throw new RuntimeException(InterfaceMsg.AUTH_ERROR.getMsg());
        }
    }
}
