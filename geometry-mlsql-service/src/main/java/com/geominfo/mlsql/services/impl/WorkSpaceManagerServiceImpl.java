package com.geominfo.mlsql.services.impl;

import com.geominfo.authing.common.constants.CommonConstants;
import com.geominfo.authing.common.constants.ResourceTypeConstants;
import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.dao.WorkSpaceEnginesMapper;
import com.geominfo.mlsql.domain.param.WorkSpaceInfoParam;
import com.geominfo.mlsql.domain.param.WorkSpaceMemberParam;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.po.WorkSpaceEngine;
import com.geominfo.mlsql.domain.po.WorkSpaceMember;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.result.WorkSpaceInfoResult;
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
    @Autowired
    private WorkSpaceEnginesMapper workSpaceEnginesMapper;

    @Override
    public BaseResultVo insertWorkSpace(WorkSpaceInfoParam workSpaceInfoParam) {
        BaseResultVo baseResultVo = new BaseResultVo();
        TSystemResources systemResources = new TSystemResources();
        systemResources.setResourceName(workSpaceInfoParam.getWorkSpaceName());
        systemResources.setDisplayName(workSpaceInfoParam.getWorkSpaceName());
        systemResources.setParentid(workSpaceInfoParam.getSpaceOwnerId());
        systemResources.setResourceTypeId(new BigDecimal(ResourceTypeConstants.GZKJ_ML));
        systemResources.setOwner(workSpaceInfoParam.getSpaceOwnerId());
        systemResources.setContentInfo(workSpaceInfoParam.getDescribe());
        baseResultVo = systemResourceService.insertResource(systemResources);
        if (baseResultVo.isSuccess()) {
            BigDecimal spaceId = baseResultVo.getId();
            WorkSpaceMemberParam workSpaceMemberParam = new WorkSpaceMemberParam();
            workSpaceMemberParam.setSpaceId(spaceId);
            workSpaceMemberParam.setSpaceOwnerId(workSpaceInfoParam.getSpaceOwnerId());
            workSpaceMemberParam.setSpaceMemberId(workSpaceInfoParam.getSpaceOwnerId());
            baseResultVo = workSpaceMemberService.insertSpaceMember(workSpaceMemberParam);
            if (baseResultVo.isSuccess()) {
                baseResultVo.setReturnMsg(InterfaceMsg.INSERT_SPACE_SUCCESS.getMsg());
                return baseResultVo;
            } else {
                baseResultVo.setReturnMsg(InterfaceMsg.INSERT_SPACE_FAIL.getMsg());
                return baseResultVo;
            }
        } else {
            baseResultVo.setReturnMsg(InterfaceMsg.INSERT_SPACE_FAIL.getMsg());
            return baseResultVo;
        }
    }

    @Override
    public BaseResultVo updateWorkSpace(WorkSpaceInfoParam workSpaceInfoParam) {
        BaseResultVo baseResultVo = new BaseResultVo();
        TSystemResources systemResources = new TSystemResources();
        if (workSpaceInfoParam.getSpaceId() == null) {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(CommonConstants.PARAM_ERROR);
            return baseResultVo;
        }
        systemResources.setId(workSpaceInfoParam.getSpaceId());
        systemResources.setResourceTypeId(new BigDecimal(ResourceTypeConstants.GZKJ_ML));
        if (StringUtils.isNotBlank(workSpaceInfoParam.getWorkSpaceName()))
            systemResources.setResourceName(workSpaceInfoParam.getWorkSpaceName());
        if (StringUtils.isNotBlank(workSpaceInfoParam.getDescribe()))
            systemResources.setContentInfo(workSpaceInfoParam.getDescribe());
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
            if (!defaultSpace.getSpaceId().equals(spaceId)) {
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
    public List<WorkSpaceInfoResult> getWorkSpaceLists(Integer userId) {
        List<WorkSpaceInfoResult> workSpaceInfoVos = systemResourcesDao.getWorkSpaceLists(userId);
        BaseResultVo baseResultVo = new BaseResultVo();
        //查询用户工作空间，若无工作空间，则初始化默认空间
        if (CollectionUtils.isEmpty(workSpaceInfoVos)){
            if (initWorkSpace(BigDecimal.valueOf(userId))){
                workSpaceInfoVos = systemResourcesDao.getWorkSpaceLists(userId);
                for (WorkSpaceInfoResult vo : workSpaceInfoVos) {
                    getWorkSpaceInfo(vo);
                }
                return workSpaceInfoVos;
            }else {
                throw new RuntimeException(InterfaceMsg.QUERY_ERROR.getMsg());
            }
        }else {
            //默认查询用户所有的工作空间
            for (WorkSpaceInfoResult vo : workSpaceInfoVos) {
                getWorkSpaceInfo(vo);
            }
            return workSpaceInfoVos;
        }
    }

    @Override
    public List<WorkSpaceInfoResult> getWorkSpaceListsByName(BigDecimal userId, String spaceName) {
        if (StringUtils.isBlank(spaceName))
            spaceName = null;
        return systemResourcesDao.getWorkSpaceListsByName(userId,spaceName);
    }

    @Override
    public void deleteEngine(BigDecimal engineId, BigDecimal spaceId) {
        workSpaceEnginesMapper.deleteEngine(engineId,spaceId);
    }

    @Override
    public BaseResultVo insertWorkSpaceEngine(WorkSpaceEngine workSpaceEngine) {
        BaseResultVo baseResultVo = new BaseResultVo();
        List<WorkSpaceEngine> workSpaceEngines = workSpaceEnginesMapper.getSpaceEngineBySpaceIdAndEngineId(workSpaceEngine.getEngineId(),workSpaceEngine.getWorkspaceId());
        if (CollectionUtils.isEmpty(workSpaceEngines)){
            int i = workSpaceEnginesMapper.insert(workSpaceEngine);
            if (i > 0){
                baseResultVo.setSuccess(Boolean.TRUE);
                baseResultVo.setReturnMsg(InterfaceMsg.INSERT_SPACE_ENGINE_SUCCESS.getMsg());
                return baseResultVo;
            }else {
                baseResultVo.setSuccess(Boolean.FALSE);
                baseResultVo.setReturnMsg(InterfaceMsg.INSERT_SPACE_ENGINE_FAIL.getMsg());
                return baseResultVo;
            }
        }else {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(CommonConstants.PARAM_ERROR);
            return baseResultVo;
        }


    }

    @Override
    public BaseResultVo setDefaultEngine(BigDecimal engineId, BigDecimal spaceId) {
        BaseResultVo baseResultVo = new BaseResultVo();
        int i = workSpaceEnginesMapper.setDefaultEngine(engineId,spaceId);
        if (i > 0){
            baseResultVo.setSuccess(Boolean.TRUE);
            baseResultVo.setReturnMsg(InterfaceMsg.SET_DEFAULT_ENGINE_SUCCESS.getMsg());
            return baseResultVo;
        }else {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(InterfaceMsg.SET_DEFAULT_ENGINE_FAIL.getMsg());
            return baseResultVo;

        }
    }

    @Override
    public Boolean initWorkSpace(BigDecimal userId) {
        BaseResultVo baseResultVo = new BaseResultVo();
        TSystemResources systemResources = new TSystemResources();
        systemResources.setResourceName(CommonConstants.DEFAULT_SPACE_NAME);
        systemResources.setDisplayName(CommonConstants.DEFAULT_SPACE_NAME);
        systemResources.setParentid(userId);
        systemResources.setResourceTypeId(new BigDecimal(ResourceTypeConstants.GZKJ_ML));
        systemResources.setOwner(userId);
        systemResources.setContentInfo(CommonConstants.INIT_DATA_SUCCESS);
        baseResultVo = systemResourceService.insertResource(systemResources);
        if (baseResultVo.isSuccess()) {
            BigDecimal spaceId = baseResultVo.getId();
            WorkSpaceMemberParam workSpaceMemberParam = new WorkSpaceMemberParam();
            workSpaceMemberParam.setSpaceId(spaceId);
            workSpaceMemberParam.setSpaceOwnerId(userId);
            workSpaceMemberParam.setSpaceMemberId(userId);
            workSpaceMemberParam.setSpaceState("1");
            baseResultVo = workSpaceMemberService.insertSpaceMember(workSpaceMemberParam);
            if (baseResultVo.isSuccess()) {
               return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void getWorkSpaceInfo(WorkSpaceInfoResult vo){

        BigDecimal ownerId = vo.getSpaceOwnerId();
        User user = FeignUtils.parseObject(authQueryApiService.getUserById(ownerId), User.class);
        if (user != null) {
            if (StringUtils.isNotBlank(user.getLoginName())) {
                vo.setOwnerName(user.getLoginName());
            }else {
                vo.setOwnerName(user.getUserName());
            }
        }else {
            throw new RuntimeException(CommonConstants.AUTH_ERROR);
        }
    }
}
