package com.geominfo.mlsql.service.user;

import com.geominfo.mlsql.domain.vo.MlsqlUser;

import java.util.List;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: 用户接口
 * @author: anan
 * @create: 2020-06-03 12:00
 * @version: 1.0.0
 */
public interface UserService {
    /**
     * description:获取所有用户
     * author: anan
     * date: 2020/6/3
     * param:
     * return: List<MlsqlUser>
     */
    List<MlsqlUser> getAllUsers();
    /**
     * description:分页获取用户
     * author: anan
     * date: 2020/6/3
     * param: [pageNum, pageSize]
     * return: List<MlsqlUser>
     */
    List<MlsqlUser> getUserByPage(int pageNum, int pageSize);
    /**
      * description:用户登录接口
      * author: anan
      * date: 2020/6/3
      * param: [userName]
      * return: MlsqlUser
     */
    MlsqlUser userLogin(String userName);
}
