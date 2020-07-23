package com.geominfo.mlsql.service.user;

import com.geominfo.mlsql.domain.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: TeamRoleService
 * @author: anan
 * @create: 2020-07-09 16:11
 * @version: 1.0.0
 */
public interface TeamRoleService {
    /**
      * description:check team is|no exists
      * author: anan
      * date: 2020/7/14
      * param:teamName
      * return:boolean
     */

    boolean checkTeamNameValid(String teamName);
    /**
      * description:insert team
      * author: anan
      * date: 2020/7/14
      * param: teamName
      * return: int
     */

    int insertGroup(String teamName);
    /**
      * description:register user ，create default team and role
      * author: anan
      * date: 2020/7/14
      * param:MlsqlUser user, String TeamName
      * return: void
     */

    void createDefaultTeamAndRole(MlsqlUser user, String TeamName);
    /**
      * description: get role role
      * author: anan
      * date: 2020/7/14
      * param:map
      * return:MlsqlGroupRole
     */
    MlsqlGroupRole getGroupRole(Map<String,Object> map);
    /**
      * description:insert mlsql_group_role
      * author: anan
      * date: 2020/7/14
      * param:MlsqlGroupRole
      * return:int
     */

    int insertGroupRole(MlsqlGroupRole mlsqlGroupRole);
    /**
      * description:get role members
      * author: anan
      * date: 2020/7/14
      * param:map
      * return:List<Map<String,Object>>
     */

    List<Map<String,Object>> roleMembers(Map<String,Object> map);
    /**
      * description:insert role member
      * author: anan
      * date: 2020/7/14
      * param:MlsqlRoleMember
      * return:int
     */

    int insertRoleMember(MlsqlRoleMember mlsqlRoleMember);

    /**
     * description:create team
     * author: anan
     * date: 2020/7/14
     * param:MlsqlUser user, String teamName
     * return:String
     */
    String createTeam(MlsqlUser user, String teamName);

    /**
     * description:add roles
     * author: anan
     * date: 2020/7/14
     * param:teamName，roleNames
     * return:string
     */
    String addRoles(String teamName, List<String> roleNames);
    /**
     * description: add role member
     * author: anan
     * date: 2020/7/14
     * param:teamName roleName userName
     * return:String
     */
    String addMemberForRole(String teamName, String roleName, String userName);
    /**
     * description:get teams
     * author: anan
     * date: 2020/7/14
     * param:user,status
     * return:List<String>
     */
    List<String> teams(MlsqlUser user, int status);
    /**
     * description:by name get group
     * author: anan
     * date: 2020/7/14
     * param:teamName
     * return:MlsqlGroup
     */
    MlsqlGroup getGroupByName(String teamName);
    /**
     * description:add team member
     * author: anan
     * date: 2020/7/14
     * param:teamName userNames
     * return:String
     */
    String addMember(String teamName, List<String> userNames);
    /**
     * description:update member stauts
     * author: anan
     * date: 2020/7/14
     * param:user\teamName\status
     * return:string
     */
    String updateMemberStatus(MlsqlUser user, String teamName, int status);
    /**
     * description:
     * author: anan
     * date: 2020/7/14
     * param:
     * return:
     */
    String removeMember(String teamName, String userName);
    /**
     * description:get group user list
     * author: anan
     * date: 2020/7/14
     * param:mlsqlGroup
     * return:List<Map<String, Object>>
     */
    List<Map<String, Object>> getGroupUserList(MlsqlGroup mlsqlGroup);
    /**
     * description:get group role list
     * author: anan
     * date: 2020/7/14
     * param:mlsql_group_id
     * return:List<String>
     */
    List<String> getGroupRoleList(int mlsql_group_id);
    /**
     * description: remove group role
     * author: anan
     * date: 2020/7/14
     * param:mlsqlGroupRole
     * return:int
     */
    int deleteGroupRole(MlsqlGroupRole mlsqlGroupRole);
    /**
     * description:remove role member
     * author: anan
     * date: 2020/7/14
     * param:MlsqlRoleMember
     * return:int
     */
    int deleteRoleMember(MlsqlRoleMember mlsqlRoleMember);
    /**
     * description:get team backends
     * author: anan
     * date: 2020/7/14
     * param:MlsqlGroup
     * return:List<String>
     */
    List<String> backends(MlsqlGroup mlsqlGroup);
    /**
      * description: 
      * author: anan
      * date: 2020/7/22
      * param:
      * return: 
     */
    
    String getRolesByUserName(String userName);
    /**
      * description: 
      * author: anan
      * date: 2020/7/23
      * param:
      * return: 
     */
    
    String getRolesAll();
}
