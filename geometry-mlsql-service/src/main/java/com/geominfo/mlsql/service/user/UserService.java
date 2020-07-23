package com.geominfo.mlsql.service.user;

import com.geominfo.mlsql.domain.vo.Account;
import com.geominfo.mlsql.domain.vo.MlsqlUser;

import java.util.List;
import java.util.Map;

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
    List<MlsqlUser> getAllUsers(Map<String,Object> map);
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
    MlsqlUser getUserByName(String userName);

    /**
      * description: update password
      * author: anan
      * date: 2020/7/9
      * param: [userName,newPassword]
      * return: int
     */
    int changePassword(String userName, String newPassword);
    /**
      * description: register user
      * author: anan
      * date: 2020/7/9
      * param:
      * return:
     */
    int register(MlsqlUser user);
    /**
      * description:
      * author: anan
      * date: 2020/7/10
      * param:
      * return:
     */
    int updateUser(MlsqlUser mlsqlUser);
    public Account loadAccount(String appId);
}
