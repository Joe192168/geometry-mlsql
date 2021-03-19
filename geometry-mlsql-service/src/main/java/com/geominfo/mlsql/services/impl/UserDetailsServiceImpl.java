package com.geominfo.mlsql.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.geominfo.mlsql.domain.dto.JwtUser;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.services.dao.IRoleDao;
import com.geominfo.mlsql.services.dao.IUserDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserDao iUserDao;

    @Autowired
    private IRoleDao iRoleDao;

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        log.info(loginName);
        User user = iUserDao.getUserByLoginName(loginName);
        if (user != null) {
            List<String> permList = new ArrayList<>();
            //这块是获取用户权限，也可以改成RBAC（角色控制URL权限)
            String roleStr = iRoleDao.getUserRole(user.getLoginName());
            List<String> roleList = Arrays.asList(roleStr.split(","));
            for (String role:roleList){
                permList.add(role);
            }
            String[] array = new String[roleList.size()];
            permList.toArray(array);
            user.setPermissions(array);
            return new JwtUser(user);
        } else {
            throw new UsernameNotFoundException("该用户: " + loginName + " 不存在!");
        }
    }
}