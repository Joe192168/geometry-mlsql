package com.geominfo.mlsql.services.impl;

import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.services.AuthApiService;
import com.geominfo.mlsql.services.dao.IUserDao;
import com.geominfo.mlsql.utils.FeignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserServiceImpl")
public class UserServiceImpl implements IUserDao {

    @Autowired
    private AuthApiService authApiService;

    @Override
    public User getUserByLoginName(String loginName) {
        return FeignUtils.parseObject(authApiService.getUserByLoginName(loginName),User.class);
    }
}
