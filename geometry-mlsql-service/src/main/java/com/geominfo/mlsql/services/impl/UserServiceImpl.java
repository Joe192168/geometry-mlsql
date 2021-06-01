package com.geominfo.mlsql.services.impl;

import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.services.AuthQueryApiService;
import com.geominfo.mlsql.services.dao.IUserDao;
import com.geominfo.mlsql.utils.FeignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserServiceImpl")
public class UserServiceImpl implements IUserDao {

    @Autowired
    private AuthQueryApiService authQueryApiService;

    @Override
    public User getUserByLoginName(String loginName) {
        return FeignUtils.parseObject(authQueryApiService.getUserByLoginName(loginName),User.class);
    }
}
