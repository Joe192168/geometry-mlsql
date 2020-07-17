package com.geominfo.mlsql.service.user.impl;

import com.geominfo.mlsql.domain.vo.*;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.mapper.TeamRoleMapper;
import com.geominfo.mlsql.mapper.UserMapper;
import com.geominfo.mlsql.service.user.TeamRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: TeamRoleServiceImpl
 * @author: anan
 * @create: 2020-07-09 16:12
 * @version: 1.0.0
 */
@Service
public class TeamRoleServiceImpl implements TeamRoleService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    TeamRoleMapper teamRoleMapper;

    @Override
    public boolean checkTeamNameValid(String teamName) {
        return teamRoleMapper.getGroupByName(teamName) == null?true:false;
    }

    @Override
    public int insertGroup(String teamName) {
        return teamRoleMapper.insertGroup(teamName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDefaultTeamAndRole(MlsqlUser user, String teamName) {
        createTeam(user, teamName);
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole());
        addRoles(teamName, roles);
        addMemberForRole(teamName, user.getRole(), user.getName());
    }

    @Override
    public MlsqlGroupRole getGroupRole(Map<String,Object> map) {
        return teamRoleMapper.getGroupRole(map);
    }

    @Override
    public int insertGroupRole(MlsqlGroupRole mlsqlGroupRole) {
        return teamRoleMapper.insertGroupRole(mlsqlGroupRole);
    }

    @Override
    public List<Map<String,Object>> roleMembers(Map<String,Object> map) {
        List<MlsqlRoleMember> mlsqlRoleMemberList = teamRoleMapper.getRoleMember(map);
        Map<String,Object> map1 = new HashMap<>();
        List<Integer> userIds = new ArrayList<Integer>();
        for(MlsqlRoleMember mlsqlRoleMember : mlsqlRoleMemberList){
            userIds.add(mlsqlRoleMember.getUserId());
        }
        List<Map<String,Object>> members = new ArrayList<>();
        if(userIds.size()>0){
            map1.put("id", userIds);
            members = userMapper.getUserByIdList(map1);
        }

        return members;
    }

    @Override
    public int insertRoleMember(MlsqlRoleMember mlsqlRoleMember) {
        return teamRoleMapper.insertRoleMember(mlsqlRoleMember);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createTeam(MlsqlUser user, String teamName) {
        if(teamRoleMapper.getGroupByName(teamName) != null){
            return ReturnCode.TEAM_EXISTS;
        }
        teamRoleMapper.insertGroup(teamName);
        MlsqlGroup mlsqlGroup = teamRoleMapper.getGroupByName(teamName);
        MlsqlGroupUser mlsqlGroupUser = new MlsqlGroupUser();
        mlsqlGroupUser.setGroupId(mlsqlGroup.getId());
        mlsqlGroupUser.setUserId(user.getId());
        mlsqlGroupUser.setStatus(ReturnCode.UserOwner);
        teamRoleMapper.insertGroupUser(mlsqlGroupUser);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addRoles(String teamName, List<String> roleNames) {
        MlsqlGroup mlsqlGroup = teamRoleMapper.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return ReturnCode.TEAM_NOT_EXISTS;
        }
        for (String roleName : roleNames){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("roleName", roleName );
            map.put("mlsql_group_id", mlsqlGroup.getId() );
            if(teamRoleMapper.getGroupRole(map) == null){
                MlsqlGroupRole mlsqlGroupRole = new MlsqlGroupRole();
                mlsqlGroupRole.setName(roleName);
                mlsqlGroupRole.setGroupId(mlsqlGroup.getId());
                teamRoleMapper.insertGroupRole(mlsqlGroupRole);
            }
        }
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addMemberForRole(String teamName, String roleName, String userName) {
        MlsqlUser user = userMapper.getUserByName(userName);
        MlsqlGroup mlsqlGroup = teamRoleMapper.getGroupByName(teamName);
        if(user == null || mlsqlGroup == null){
            return ReturnCode.TEAM_OR_USER_NOT_EXISTS;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", roleName );
        map.put("groupId", mlsqlGroup.getId() );
        MlsqlGroupRole mlsqlGroupRole = teamRoleMapper.getGroupRole(map);
        if(mlsqlGroupRole == null){
            return ReturnCode.TEAM_ROLE_NOT_EXISTS;
        }
        map = new HashMap<String,Object>();
        map.put("userId", user.getId() );
        map.put("groupRoleId", mlsqlGroupRole.getId());
        List<MlsqlRoleMember> mlsqlRoleMemberList = teamRoleMapper.getRoleMember(map);
        if(mlsqlRoleMemberList == null || mlsqlRoleMemberList.size() == 0){
            MlsqlRoleMember mlsqlRoleMember = new MlsqlRoleMember();
            mlsqlRoleMember.setUserId(user.getId());
            mlsqlRoleMember.setGroupRoleId(mlsqlGroupRole.getId());
            teamRoleMapper.insertRoleMember(mlsqlRoleMember);
        }
        return ReturnCode.SUCCESS;
    }

    @Override
    public List<String> teams(MlsqlUser user, int status) {
        MlsqlGroupUser mlsqlGroupUser = new MlsqlGroupUser();
        mlsqlGroupUser.setUserId(user.getId());
        mlsqlGroupUser.setStatus(status);
        List<MlsqlGroup> mlsqlGroupList =  teamRoleMapper.getGroups(mlsqlGroupUser);
        List<String> teams = new ArrayList<>();
        for(MlsqlGroup mlsqlGroup : mlsqlGroupList){
            teams.add(mlsqlGroup.getName());
        }
        return teams;
    }

    @Override
    public MlsqlGroup getGroupByName(String teamName) {
        return teamRoleMapper.getGroupByName(teamName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addMember(String teamName, List<String> userNames) {
        MlsqlGroup mlsqlGroup = getGroupByName(teamName);
        if(mlsqlGroup == null){
            return ReturnCode.TEAM_NOT_EXISTS;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userNames", userNames);
        List<MlsqlUser> users = userMapper.getAllUsers(map);
        if (users.size() != userNames.size()) {
            return ReturnCode.USER_NOT_EXISTS;
        }
        for (MlsqlUser user : users) {
            MlsqlGroupUser mlsqlGroupUser = new MlsqlGroupUser();
            mlsqlGroupUser.setUserId(user.getId());
            mlsqlGroupUser.setGroupId(mlsqlGroup.getId());
            MlsqlGroupUser mlsqlGroupUserD = teamRoleMapper.getGroupUser(mlsqlGroupUser);
            if(mlsqlGroupUserD == null){
                mlsqlGroupUser.setStatus(MlsqlGroupUser.invited);
                teamRoleMapper.insertGroupUser(mlsqlGroupUser);
            }else
            {
                if(mlsqlGroupUserD.getStatus() == MlsqlGroupUser.refused)
                {
                    mlsqlGroupUser.setStatus(MlsqlGroupUser.invited);
                    teamRoleMapper.updateGroupUser(mlsqlGroupUser);
                }

            }
        }
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateMemberStatus(MlsqlUser user, String teamName, int status) {
        MlsqlGroup mlsqlGroup = teamRoleMapper.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return ReturnCode.TEAM_NOT_EXISTS;
        }
        MlsqlGroupUser mlsqlGroupUserR = new MlsqlGroupUser();
        mlsqlGroupUserR.setGroupId(mlsqlGroup.getId());
        mlsqlGroupUserR.setUserId(user.getId());
        MlsqlGroupUser mlsqlGroupUser = teamRoleMapper.getGroupUser(mlsqlGroupUserR);
        mlsqlGroupUser.setStatus(status);
        teamRoleMapper.updateGroupUser(mlsqlGroupUser);
        return ReturnCode.SUCCESS;
    }

    @Override
    public String removeMember(String teamName, String userName) {
        MlsqlGroup mlsqlGroup = teamRoleMapper.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return ReturnCode.TEAM_NOT_EXISTS;
        }
        MlsqlUser mlsqlUser = userMapper.getUserByName(userName);
        if(mlsqlUser == null){
            return ReturnCode.USER_NOT_EXISTS;
        }
        MlsqlGroupUser mlsqlGroupUserR = new MlsqlGroupUser();
        mlsqlGroupUserR.setUserId(mlsqlUser.getId());
        mlsqlGroupUserR.setGroupId(mlsqlGroup.getId());
        teamRoleMapper.deleteGroupUser(mlsqlGroupUserR);
        return ReturnCode.SUCCESS;
    }

    @Override
    public  List<Map<String, Object>> getGroupUserList(MlsqlGroup mlsqlGroup) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(MlsqlGroupUser.owner);
        statusList.add(MlsqlGroupUser.confirmed);
        map.put("mlsql_group_id", mlsqlGroup.getId());
        map.put("status", statusList);
        List<MlsqlGroupUser> mlsqlGroupUserList = teamRoleMapper.getGroupUserList(map);
        List<Map<String, Object>> members = new ArrayList<Map<String, Object>>();
        for(MlsqlGroupUser mlsqlGroupUser : mlsqlGroupUserList){
            map = new HashMap<String, Object>();
            map.put("name", mlsqlGroupUser.getMlsqlUser().getName());
            map.put("status", mlsqlGroupUser.getStatus());
            members.add(map);
        }
        return members;
    }

    @Override
    public List<String> getGroupRoleList(int mlsql_group_id) {
        List<MlsqlGroupRole> mlsqlGroupRoleList = teamRoleMapper.getGroupRoleList(mlsql_group_id);
        List<String> roleNames = new ArrayList<>();
        for(MlsqlGroupRole mlsqlGroupRole : mlsqlGroupRoleList){
            roleNames.add(mlsqlGroupRole.getName());
        }
        return roleNames;
    }

    @Override
    public int deleteGroupRole(MlsqlGroupRole mlsqlGroupRole) {
        return teamRoleMapper.deleteGroupRole(mlsqlGroupRole);
    }

    @Override
    public int deleteRoleMember(MlsqlRoleMember mlsqlRoleMember) {
        return teamRoleMapper.deleteRoleMember(mlsqlRoleMember);
    }

    @Override
    public List<String> backends(MlsqlGroup mlsqlGroup) {
        MlsqlBackendProxy mlsqlBackendProxy = new MlsqlBackendProxy();
        mlsqlBackendProxy.setGroupId(mlsqlGroup.getId());
        List<String> mlsqlBackendProxyList = teamRoleMapper.getGroupBackendProxy(mlsqlBackendProxy);
        return mlsqlBackendProxyList;
    }
}
