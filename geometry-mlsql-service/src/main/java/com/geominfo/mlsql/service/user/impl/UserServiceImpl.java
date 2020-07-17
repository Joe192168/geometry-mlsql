package com.geominfo.mlsql.service.user.impl;

import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.mapper.UserMapper;
import com.geominfo.mlsql.service.user.UserService;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: 用户接口
 * @author: anan
 * @create: 2020-06-03 12:00
 * @version: 1.0.0
 */
@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<MlsqlUser> getAllUsers(Map<String,Object> map) {
        return userMapper.getAllUsers(map);
    }

    @Override
    public List<MlsqlUser> getUserByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Map<String,Object> map = new HashMap<String,Object>();
        List<MlsqlUser> lists = userMapper.getAllUsers(map);
        return lists;
    }

    @Override
    public MlsqlUser getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }

    @Override
    public int changePassword(String userName, String newPassword) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userName", userName);
        map.put("newPassword", newPassword);
        return userMapper.changePassword(map);
    }

    @Override
    public int register(MlsqlUser user) {
        return userMapper.register(user);
    }

    @Override
    public int updateUser(MlsqlUser mlsqlUser) {
        return userMapper.updateUser(mlsqlUser);
    }
}
