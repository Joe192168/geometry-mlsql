package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: Test
 * @author: anan
 * @create: 2020-06-03 11:43
 * @version: 1.0.0
 */
@Mapper
@Component
public interface UserMapper {
    /**
      * description: 用户登录接口
      * author: anan
      * date: 2020/6/3
      * param:[userName, password]
      * return: void
     */
    MlsqlUser getUserByName(@Param(value = "name") String  userName);
    /**
     * description: 获取所有用户
     * author: anan
     * date: 2020/6/3
     * param:
     * return: void
     */

    List<MlsqlUser> getAllUsers(Map<String,Object> map);
    /**
      * description: update password
      * author: anan
      * date: 2020/7/9
      * param:
      * return:
     */
    int changePassword(Map<String,Object> map);

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
      * date: 2020/7/15
      * param:
      * return:
     */
    List<Map<String, Object>> getUserByIdList(Map<String, Object> map);

    /**
      * description:
      * author: anan
      * date: 2020/7/15
      * param:
      * return:
     */
    MlsqlUser getUserById(int id);
    /**
     * description:
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    int updateUser(MlsqlUser mlsqlUser);
}
