package com.geominfo.mlsql.controller.user;

import com.geominfo.mlsql.domain.vo.*;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.service.user.TeamRoleService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.utils.MD5Scala;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: TeamRoleController
 * @author: anan
 * @create: 2020-07-09 15:48
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/api_v1")
@Api(value="用户组角色维护类",tags={"用户组角色维护接口"})
@Log4j2
public class TeamRoleController {
    @Autowired
    private TeamRoleService teamRoleService;

    @Autowired
    private UserService userService;

    @RequestMapping("/team/create")
    @ApiOperation(value = "新增组及成员", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true)
    })
    public Message createTeam(@RequestParam(value = "teamName", required = true) String teamName,
                               @RequestParam(value = "userName", required = true) String userName){
        MlsqlUser mlsqlUser = userService.getUserByName(userName); //replace token
        String res = teamRoleService.createTeam(mlsqlUser, teamName);
        return res.equals(ReturnCode.SUCCESS) == true
                ? new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"add new team").addData("data",res)
                : new Message().error(ReturnCode.RETURN_ERROR_STATUS,res);

    }

    @RequestMapping("/team/name/check")
    @ApiOperation(value = "检查组是否存在", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true)
    })
    public Message checkTeamIsExists(@RequestParam(value = "teamName", required = true) String teamName){
        boolean res = teamRoleService.checkTeamNameValid(teamName);
        return res == true
                ? new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"teamName no exists").addData("data",res)
                : new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"teamName is exists").addData("data",res);
    }

    @RequestMapping("/team")
    @ApiOperation(value = "获取用户所属组", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true)
    })
    public Message team(@RequestParam(value = "userName", required = true) String userName){

        MlsqlUser user = userService.getUserByName(userName); //replace token
        List<String> groups = teamRoleService.teams(user, MlsqlGroupUser.owner);
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get owner sucess").addData("data",groups);
    }

    @RequestMapping("/team/teamsIn")
    @ApiOperation(value = "获取用户组", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true)
    })
    public Message teamsIn(@RequestParam(value = "userName", required = true) String userName){

        MlsqlUser user = userService.getUserByName(userName); //后续要代替token中的user
        List<String> groups = teamRoleService.teams(user, MlsqlGroupUser.confirmed);
        groups.addAll(teamRoleService.teams(user, MlsqlGroupUser.owner));
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get teams sucess").addData("data",groups);
    }

    @RequestMapping("/team/joined")
    @ApiOperation(value = "获取被邀请已加入组", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true)
    })
    public Message joinedTeam(@RequestParam(value = "userName", required = true) String userName){

        MlsqlUser user = userService.getUserByName(userName); //后续要代替token中的user
        List<String> groups = teamRoleService.teams(user, MlsqlGroupUser.confirmed);
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get confirmed teams sucess").addData("data",groups);
    }
    
    @RequestMapping("/team/invited")
    @ApiOperation(value = "获取被邀请还未确认组名", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true)
    })
    public Message invitedTeam(@RequestParam(value = "userName", required = true) String userName){

        MlsqlUser user = userService.getUserByName(userName); //后续要代替token中的user
        List<String> groups = teamRoleService.teams(user, MlsqlGroupUser.invited);
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get invited user team").addData("data",groups);
    }

    @RequestMapping("/team/member/add")
    @ApiOperation(value = "新增组成员", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "用户名列表(多个逗号分隔)", name = "userNames", dataType = "String", paramType = "query", required = true)
    })
    public Message teamMemberAdd(@RequestParam(value = "teamName", required = true) String teamName,
                                  @RequestParam(value = "userNames", required = true) String userNames){

        String res =  teamRoleService.addMember(teamName, Arrays.asList(userNames.split(",")));
        return res.equals(ReturnCode.SUCCESS) == true
                ? new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"user add team").addData("data",res)
                : new Message().error(ReturnCode.RETURN_ERROR_STATUS,res);
    }

    @RequestMapping("/team/role/add")
    @ApiOperation(value = "新增角色", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "角色列表(多个逗号分隔)", name = "roleNames", dataType = "String", paramType = "query", required = true)
    })
    public Message teamRoleAdd(@RequestParam(value = "teamName", required = true) String teamName,
                                @RequestParam(value = "roleNames", required = true) String roleNames){

        String res = teamRoleService.addRoles(teamName, Arrays.asList(roleNames.split(",")));
        return res.equals(ReturnCode.SUCCESS) == true
                ? new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"team add role").addData("data",res)
                : new Message().error(ReturnCode.RETURN_ERROR_STATUS,res);
    }

    @RequestMapping("/team/member/accept")
    @ApiOperation(value = "用户被邀请同意操作", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true)
    })
    public Message accpetTeamMemberAdd(@RequestParam(value = "teamName", required = true) String teamName,
                                        @RequestParam(value = "userName", required = true) String userName){
        MlsqlUser user = userService.getUserByName(userName);//replace token
        String res = teamRoleService.updateMemberStatus(user, teamName, MlsqlGroupUser.confirmed);
        return res.equals(ReturnCode.SUCCESS) == true
                ? new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"update member accept status").addData("data",res)
                : new Message().error(ReturnCode.RETURN_ERROR_STATUS,res);
    }

    @RequestMapping("/team/member/remove")
    @ApiOperation(value = "删除组成员", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true)
    })
    public Message teamMemberRemove(@RequestParam(value = "teamName", required = true) String teamName,
                                     @RequestParam(value = "userName", required = true) String userName){
        String res = teamRoleService.removeMember(teamName, userName);
        return res.equals(ReturnCode.SUCCESS) == true
                ? new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"delete team member").addData("data",res)
                : new Message().error(ReturnCode.RETURN_ERROR_STATUS,res);
    }

    @RequestMapping("/team/member/refuse")
    @ApiOperation(value = "用户被邀请拒绝操作", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true)
    })
    public Message refuseTeamMemberAdd(@RequestParam(value = "teamName", required = true) String teamName,
                                        @RequestParam(value = "userName", required = true) String userName){
        MlsqlUser user = userService.getUserByName(userName);//replace token
        String res = teamRoleService.updateMemberStatus(user, teamName, MlsqlGroupUser.refused);
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"update member refuse status").addData("data",res);
    }

    @RequestMapping("/team/members")
    @ApiOperation(value = "获取组成员", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true)
    })
    public Message teamMembers(@RequestParam(value = "teamName", required = true) String teamName){

        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS, ReturnCode.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        List<Map<String, Object>> groupUsers = teamRoleService.getGroupUserList(mlsqlGroup);
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get group member").addData("data",groupUsers);
    }

    @RequestMapping("/team/roles")
    @ApiOperation(value = "获取角色", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true)
    })
    public Message teamRoles(@RequestParam(value = "teamName", required = true) String teamName){
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS, ReturnCode.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        List<String> roleList = teamRoleService.getGroupRoleList(mlsqlGroup.getId());
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get roles").addData("data",roleList);
    }

    @RequestMapping("/team/role/remove")
    @ApiOperation(value = "删除角色", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "角色名", name = "roleName", dataType = "String", paramType = "query", required = true)
    })
    public Message teamRoleRemove(@RequestParam(value = "teamName", required = true) String teamName,
                                   @RequestParam(value = "roleName", required = true) String roleName){
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS, ReturnCode.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        MlsqlGroupRole mlsqlGroupRole = new MlsqlGroupRole();
        mlsqlGroupRole.setGroupId(mlsqlGroup.getId());
        mlsqlGroupRole.setName(roleName);
        int res = teamRoleService.deleteGroupRole(mlsqlGroupRole); //只删除角色，没有删除角色成员
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"remove role").addData("data",res);
    }

    @RequestMapping("/role/member/add")
    @ApiOperation(value = "添加角色", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名(多个逗号分隔）", name = "userNames", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "角色名(多个逗号分隔）", name = "roleNames", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true)
    })
    public Message RoleMemberAdd(@RequestParam(value = "userNames", required = true) String userNames,
                                  @RequestParam(value = "roleNames", required = true) String roleNames,
                                  @RequestParam(value = "teamName", required = true) String teamName){
       for (String userName : userNames.split(",")){
           for (String roleName : roleNames.split(",")){
               String res = teamRoleService.addMemberForRole(teamName, roleName, userName);
           }
       }
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"add role team member");
    }

    @RequestMapping("/role/members")
    @ApiOperation(value = "获取角色成员", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "角色名", name = "roleName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true)
    })
    public Message roleMemberList(@RequestParam(value = "roleName", required = true) String roleName,
                                   @RequestParam(value = "teamName", required = true) String teamName){
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS, ReturnCode.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", roleName);
        map.put("groupId", mlsqlGroup.getId());
        MlsqlGroupRole mlsqlGroupRole = teamRoleService.getGroupRole(map);
        if(mlsqlGroupRole == null){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS, ReturnCode.TEAM_ROLE_NOT_EXISTS).addData("data", map);
        }
        map = new HashMap<String, Object>();
        map.put("groupRoleId", mlsqlGroupRole.getId());
        List<Map<String,Object>> mapResult = teamRoleService.roleMembers(map);
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get role member").addData("data", mapResult);
    }

    @RequestMapping("/role/member/remove")
    @ApiOperation(value = "移除角色成员", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "角色名", name = "roleName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true)
    })
    public Message roleMemberRemove(@RequestParam(value = "roleName", required = true) String roleName,
                                     @RequestParam(value = "teamName", required = true) String teamName,
                                     @RequestParam(value = "userName", required = true) String userName){
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS, ReturnCode.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", roleName);
        map.put("groupId", mlsqlGroup.getId());
        MlsqlGroupRole mlsqlGroupRole = teamRoleService.getGroupRole(map);
        if(mlsqlGroupRole == null){
            return new Message().error(ReturnCode.RETURN_ERROR_STATUS, ReturnCode.TEAM_ROLE_NOT_EXISTS).addData("data", map);
        }
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        MlsqlRoleMember mlsqlRoleMember = new MlsqlRoleMember();
        mlsqlRoleMember.setUserId(mlsqlUser.getId());
        mlsqlRoleMember.setGroupRoleId(mlsqlGroupRole.getId());
        teamRoleService.deleteRoleMember(mlsqlRoleMember);
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"remove role team member").addData("data", mlsqlRoleMember);
    }

    @RequestMapping("/backends")
    @ApiOperation(value = "获取组绑定后端名称", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true)
    })
    public Message backends(@RequestParam(value = "teamName", required = true) String teamName){
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return new Message().ok(ReturnCode.RETURN_ERROR_STATUS, ReturnCode.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        List<String> mlsqlBackendProxyList = teamRoleService.backends(mlsqlGroup);
        return new Message().ok(ReturnCode.RETURN_SUCCESS_STATUS,"get team backends list").addData("data", mlsqlBackendProxyList);
    }




}
