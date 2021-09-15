package com.geominfo.mlsql.services.impl;

import com.geominfo.authing.common.enums.EnumApplicationResource;
import com.geominfo.mlsql.commons.SystemCustomIdentification;
import com.geominfo.mlsql.domain.dto.JwtUser;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.vo.SystemResourceVo;
import com.geominfo.mlsql.domain.vo.UserPermissionInfosVo;
import com.geominfo.mlsql.domain.vo.UserSessionVo;
import com.geominfo.mlsql.services.AuthApiService;
import com.geominfo.mlsql.services.AuthQueryApiService;
import com.geominfo.mlsql.services.SystemPermissionService;
import com.geominfo.mlsql.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/5/25 17:41
 * @version: 1.0.0
 */
@Service
public class SystemPermissionServiceImpl implements SystemPermissionService {
    private static final Logger logger = LoggerFactory.getLogger(SystemPermissionServiceImpl.class);

    @Autowired
    private AuthQueryApiService authQueryApiService;
    @Autowired
    private AuthApiService authApiService;

    @Override
    public UserPermissionInfosVo getUserPermissionInfos(String token, JwtUser jwtUser) {
        UserPermissionInfosVo userPermissionInfosVo = new UserPermissionInfosVo();
        User user = null;
        List<TreeVo<SystemResourceVo>> allPermissionTrees = null;
        TreeDataProcessor t = TreeDataProcessor.getInstance();
        try {
            if(null != jwtUser ){
                logger.info("getAccountPermissions request auth get userInfo param id:{}", jwtUser.getId());
                user = FeignUtils.parseObject(authQueryApiService.getUserById(new BigDecimal(jwtUser.getId())), User.class);
                logger.info("getAccountPermissions request auth get userInfo user:{}", user);
                //查询人员信息成功后，再查询人员权限数据
                if (null != user) {
                    user.setPassword(null);
                    user.setSalt(null);
                    //请求统一认证平台获取用户权限
                    logger.info("getAccountPermissions request auth get permissions param userResourceId:{},appId:{}", user.getResourceId(), EnumApplicationResource.MLSQL);
                    List allPermissions = FeignUtils.parseArray(authQueryApiService.getUserPermissions(new BigDecimal(user.getResourceId()), new BigDecimal(EnumApplicationResource.MLSQL.getResourceId())), SystemResourceVo.class);
                    logger.info("getAccountPermissions request auth get permissions resParam :{}", allPermissions);
                    if (allPermissions != null && allPermissions.size() > 0)
                        allPermissionTrees = t.getTreeVoList(allPermissions, SystemCustomIdentification.TREE_ID, SystemCustomIdentification.TREE_NAME, SystemCustomIdentification.TREE_PARENT_ID);
                }
                userPermissionInfosVo.setJwt(token);
                userPermissionInfosVo.setUser(user);
                userPermissionInfosVo.setPermissionTrees(allPermissionTrees);
                return userPermissionInfosVo;
            }else {
                return userPermissionInfosVo;
            }

        }catch (Exception e){
            logger.error("getUserPermissionInfos error:{}",e);
            throw new RuntimeException();
        }
    }

    @Override
    public void userSession(User user, String jwt, String tokenKey, HttpServletRequest request) {
        UserSessionVo userSessionVo = new UserSessionVo();
        //登录名
        userSessionVo.setLoginName(user.getLoginName());
        //用户名
        userSessionVo.setUserName(user.getUserName());
        //ip地址
        userSessionVo.setIp(IpUtil.getIpFromRequest(request));
        //会话内容
        userSessionVo.setAccessToken(jwt);
        //会话唯一标识
        userSessionVo.setTokenKey(tokenKey);
        authApiService.userSessionManager(userSessionVo);
    }
}
