package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.MlsqlUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

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
    MlsqlUser userLogin(@Param(value = "name") String  userName);
    /**
     * description: 获取所有用户
     * author: anan
     * date: 2020/6/3
     * param:
     * return: void
     */
    List<MlsqlUser> getAllUsers();

//    MlsqlUser userLogin(@Param(value = "userName") String  userName, @Param(value = "password") String password);
}
