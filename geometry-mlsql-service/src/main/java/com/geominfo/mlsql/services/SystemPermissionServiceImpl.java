package com.geominfo.mlsql.services;

import com.geominfo.mlsql.commons.SystemConstant;
import com.geominfo.mlsql.commons.SystemCustomIdentification;
import com.geominfo.mlsql.domain.dto.JwtUser;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.vo.SystemResourceVo;
import com.geominfo.mlsql.utils.FeignUtils;
import com.geominfo.mlsql.utils.TreeDataProcessor;
import com.geominfo.mlsql.utils.TreeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
    private AuthApiService authApiService;

    @Override
    public List<TreeVo<SystemResourceVo>> getAccountPermissions(JwtUser jwtUser) {
        User user = new User();
        List<TreeVo<SystemResourceVo>> allPermissionTrees = null;
        TreeDataProcessor t = TreeDataProcessor.getInstance();
        if (null != jwtUser) {
            logger.info("getAccountPermissions request auth get userInfo param id:{}", jwtUser.getId());
            user = FeignUtils.parseObject(authApiService.getUserById(new BigDecimal(jwtUser.getId())), User.class);
            logger.info("getAccountPermissions request auth get userInfo user:{}", user);
            //查询人员信息成功后，再查询人员权限数据
            if (null != user) {
                user.setPassword(null);
                user.setSalt(null);
                //请求统一认证平台获取用户权限
                logger.info("getAccountPermissions request auth get permissions param userResourceId:{},appId:{}", user.getResourceId(), SystemConstant.SYSTEM_ROOT);
                List allPermissions = FeignUtils.parseArray(authApiService.getUserPermissions(new BigDecimal(user.getResourceId()), new BigDecimal(SystemConstant.SYSTEM_ROOT)), SystemResourceVo.class);
                logger.info("getAccountPermissions request auth get permissions resParam :{}", allPermissions);
                if (allPermissions != null && allPermissions.size() > 0)
                    allPermissionTrees = t.getTreeVoList(allPermissions, SystemCustomIdentification.TREE_ID, SystemCustomIdentification.TREE_NAME, SystemCustomIdentification.TREE_PARENT_ID);
            }
            return allPermissionTrees;
        } else {
            return null;
        }
    }
}
