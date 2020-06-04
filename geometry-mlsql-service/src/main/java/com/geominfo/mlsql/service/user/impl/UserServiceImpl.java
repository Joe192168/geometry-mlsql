package com.geominfo.mlsql.service.user.impl;

import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.mapper.UserMapper;
import com.geominfo.mlsql.service.user.UserService;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * description:获取所有用户
     * author: anan
     * date: 2020/6/3
     * param:
     * return: List<MlsqlUser>
     */
    @Override
    public List<MlsqlUser> getAllUsers() {
        return userMapper.getAllUsers();
    }

    /**
     * description:分页获取用户
     * author: anan
     * date: 2020/6/3
     * param: [pageNum, pageSize]
     * return: List<MlsqlUser>
     */
    @Override
    public List<MlsqlUser> getUserByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MlsqlUser> lists = userMapper.getAllUsers();
        return lists;
    }
    /**
     * description:用户登录接口
     * author: anan
     * date: 2020/6/3
     * param: [userName]
     * return: MlsqlUser
     */
    @Override
    public MlsqlUser userLogin(String userName) {
        return userMapper.userLogin(userName);
    }
}
