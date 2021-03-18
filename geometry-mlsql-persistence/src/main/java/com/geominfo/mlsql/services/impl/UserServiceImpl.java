package com.geominfo.mlsql.services.impl;

import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.services.dao.IUserDao;
import com.geominfo.mlsql.services.feign.FeignUserService;
import com.geominfo.mlsql.utils.FeignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserServiceImpl")
public class UserServiceImpl implements IUserDao {

    @Autowired
    private FeignUserService feignUserService;

    @Override
    public User getUserByLoginName(String loginName) {
        return FeignUtils.parseObject(feignUserService.getUserByLoginName(loginName),User.class);
    }
}
