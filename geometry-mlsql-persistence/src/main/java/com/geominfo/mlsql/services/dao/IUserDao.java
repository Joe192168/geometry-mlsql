package com.geominfo.mlsql.services.dao;

import com.geominfo.mlsql.domain.pojo.User;

public interface IUserDao {

    User getUserByLoginName(String loginName);

}
