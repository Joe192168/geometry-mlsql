package com.geominfo.mlsql.services;

import com.geominfo.mlsql.domain.dto.JwtUser;
import com.geominfo.mlsql.domain.vo.SystemResourceVo;
import com.geominfo.mlsql.utils.TreeVo;

import java.util.List;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/5/25 17:38
 * @version: 1.0.0
 */
public interface SystemPermissionService {

    List<TreeVo<SystemResourceVo>> getAccountPermissions(JwtUser jwtUser);
}
