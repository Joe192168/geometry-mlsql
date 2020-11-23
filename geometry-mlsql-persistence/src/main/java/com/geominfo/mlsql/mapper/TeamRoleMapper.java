package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: TeamRoleMapper
 * @author: anan
 * @create: 2020-07-17 09:44
 * @version: 1.0.0
 */
@Mapper
@Component
public interface TeamRoleMapper {
    /**
     * description: check teamName is exists
     * author: anan
     * date: 2020/7/9
     * param:
     * return:
     */
    MlsqlGroup getGroupByName(String teamName);

    /**
     * description: create team
     * author: anan
     * date: 2020/7/9
     * param:
     * return:
     */
    int insertGroup(String teamName);

    /**
     * description: add team member
     * author: anan
     * date: 2020/7/9
     * param:
     * return:
     */
    int insertGroupUser(MlsqlGroupUser mlsqlGroupUser);

    /**
     * description: get
     * author: anan
     * date: 2020/7/10
     * param:
     * return:
     */
    MlsqlGroupRole getGroupRole(Map<String,Object> map);

    /**
     * description: insert group role
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    int insertGroupRole(MlsqlGroupRole mlsqlGroupRole);

    /**
     * description: get role member
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    List<MlsqlRoleMember> getRoleMember(Map<String,Object> map);

    /**
     * description:insert role member
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */

    int insertRoleMember(MlsqlRoleMember mlsqlRoleMember);

    /**
     * description:
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    List<MlsqlGroup> getGroups(MlsqlGroupUser mlsqlGroupUser);

    /**
     * description:
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    MlsqlGroupUser getGroupUser(MlsqlGroupUser mlsqlGroupUser);

    /**
     * description:
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    int updateGroupUser(MlsqlGroupUser mlsqlGroupUser);

    /**
     * description:
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    int deleteGroupUser(MlsqlGroupUser mlsqlGroupUser);

    /**
     * description:
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    List<MlsqlGroupUser> getGroupUserList(Map<String, Object> map);

    /**
     * description:
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    List<MlsqlGroupRole> getGroupRoleList(int mlsql_group_id);

    /**
     * description:
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    int deleteGroupRole(MlsqlGroupRole mlsqlGroupRole);

    /**
     * description:
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    int deleteRoleMember(MlsqlRoleMember mlsqlRoleMember);

    /**
     * description:
     * author: anan
     * date: 2020/7/15
     * param:
     * return:
     */
    List<String> getGroupBackendProxy(MlsqlBackendProxy mlsqlBackendProxy);
    /**
      * description: get Roles by userName
      * author: anan
      * date: 2020/7/22
      * param: userName
      * return:roles(split ,)
     */

    String getRolesByUserName(String userName);
    /**
      * description: get roles all
      * author: anan
      * date: 2020/7/23
      * param:
      * return:
     */
    String getRolesAll();
    List<MlsqlGroup> getTeamWithSchema(int userId);
}
