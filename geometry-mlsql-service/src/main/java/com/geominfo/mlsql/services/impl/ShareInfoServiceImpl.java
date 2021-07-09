package com.geominfo.mlsql.services.impl;


import com.geominfo.authing.common.constants.CommonConstants;
import com.geominfo.authing.common.constants.SystemTableConstants;
import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.dao.ShareInfoMapper;
import com.geominfo.mlsql.domain.po.ShareInfo;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.result.SharedInfoResult;
import com.geominfo.mlsql.domain.vo.QueryShareInfoVo;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.AuthQueryApiService;
import com.geominfo.mlsql.services.NumberControlService;
import com.geominfo.mlsql.services.ShareInfoService;
import com.geominfo.mlsql.utils.FeignUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/7/1 15:46
 * @version: 1.0.0
 */
@Service
public class ShareInfoServiceImpl implements ShareInfoService {

    @Autowired
    private ShareInfoMapper shareInfoMapper;
    @Autowired
    private NumberControlService numberControlService;
    @Autowired
    private AuthQueryApiService authQueryApiService;

    @Override
    public BaseResultVo insertShareInfo(ShareInfo shareInfo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        BigDecimal id = numberControlService.getMaxNum(SystemTableConstants.T_SHARE_INFO);
        shareInfo.setId(id);
        if (!BigDecimal.valueOf(1).equals(shareInfo.getShareState())){
            shareInfo.setShareState(BigDecimal.valueOf(CommonConstants.DEFAULT_INT_VAL));
        }
        shareInfo.setOperatorTime(new Date());
        shareInfo.setUpdateTime(new Date());
        int i = shareInfoMapper.insert(shareInfo);
        if (i > 0) {
            baseResultVo.setSuccess(Boolean.TRUE);
            baseResultVo.setReturnMsg(InterfaceMsg.SHARE_SCRIPT_SUCCESS.getMsg());
            baseResultVo.setId(id);
            return baseResultVo;
        } else {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(InterfaceMsg.SHARE_SCRIPT_FAIL.getMsg());
            return baseResultVo;
        }
    }

    @Override
    public List<SharedInfoResult> getShareScriptsBySharedId(Integer userId) {
        List<SharedInfoResult> sharedInfoResults = shareInfoMapper.getShareScriptsBySharedId(userId);
        for (SharedInfoResult vo :sharedInfoResults){
            getShareName(vo);
        }
        return sharedInfoResults;
    }

    @Override
    public PageInfo<List<SharedInfoResult>> getShareScriptsByUserIdAndTime(QueryShareInfoVo queryShareInfoVo) {
        PageHelper.startPage(queryShareInfoVo.getCurrentPage(), queryShareInfoVo.getPageSize());
        List<SharedInfoResult> sharedInfoResults = shareInfoMapper.getShareScriptsByUserIdAndTime(queryShareInfoVo);
        for (SharedInfoResult vo :sharedInfoResults){
            getShareName(vo);
        }
        return new PageInfo(sharedInfoResults);
    }

    private void getShareName(SharedInfoResult vo){
        BigDecimal id = vo.getShareId();
        User user = FeignUtils.parseObject(authQueryApiService.getUserById(id), User.class);
        if (user != null) {
            if (StringUtils.isNotBlank(user.getLoginName())){
                vo.setShareName(user.getLoginName());
            }else {
                vo.setShareName(user.getUserName());
            }
        }else {
            throw new RuntimeException(CommonConstants.AUTH_ERROR);
        }
    }
}
