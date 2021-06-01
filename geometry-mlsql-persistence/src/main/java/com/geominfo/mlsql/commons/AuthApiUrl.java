package com.geominfo.mlsql.commons;

/**
 * @ProjectName: geometry-bi
 * @Description: 身份认证平台接口URL维护
 * @Author: LF
 * @Date: 2020/7/24 16:02
 * @Version: 1.0.0
 */
public class AuthApiUrl {

    //查询权限池集合URL
    public static final String ROLE_PERM_RULE = "/auth_OpenApi/getSystemRoles/{systemId}";

    //根据用户登录名获取用户的角色
    public static final String GET_USER_ROLE = "/auth_OpenApi/getUserRole/{loginName}";

    //根据用户名查询，用户信息
    public static final String GET_USER_LN = "/auth_OpenApi/getUserByLoginName/{loginName}";

    //根据id查询用户信息
    public static final String USER_ID = "/auth_OpenApi/getUserById/{userId}";

    //根据用户资源id查询用户信息
    public static final String USER_RID = "/auth_OpenApi/getUserByResourceId/{resourceId}";

    //查询用户的所有权限
    public static final String GET_USER_PERMISSIONS = "/auth_OpenApi/getAPermissionsByUIdAndAId/{userId}/{appId}";

    //查询用户所有权限的id
    public static final String PERMISSION_IDS = "/auth_OpenApi/getPermissionIdsByLoginNameAndAppId/{loginName}/{appId}";

    //查询人员信息列表
    public static final String USER_LIST = "/auth_OpenApi/getUsers";

    //查询人员列表分页
    public static final String USER_LIST_PAGE = "/auth_OpenApi/getUsersPage";

    //操作日志记录接口url
    public static final String OPERATION_LOG = "/auth_OpenApi/log/save";

    //用户会话管理接口url
    public static final String USER_SESSION = "/auth_OpenApi/session/save";

//    //根据系统的clientId查询系统的系统标识
//    public static final String CLIENT_ID = "/auth_OpenApi/getProductId/{clientId}";
//
//    //发送消息接口
//    public static final String SEND_MESSAGE = "/auth_OpenApi/saveSendMessage";
//
//    //根据token获取组织机构
//    public static final String GET_OFFICE_TOKEN = "/auth_OpenApi/getOfficeByAccesskeyId/{loginName}/{accesskeyId}/{accesskeySecret}";
//
//    //根据userId获取officeId
//    public static final String QUERY_OFFICEID = "/auth_OpenApi/getOrganizationIdByUserId/{userId}";
//
//    //修改账户密码
//    public static final String UPDATE_PASSWORD = "/auth_OpenApi/update";
//
//    //修改个人设置账户
//    public static final String UPDATE_USER = "/auth_OpenApi/updateUser";
//
//    //获取识别码
//    public static final String GET_GETIDENTIFICATIONCODEBYLOGINID = "/auth_OpenApi/getIdentificationCodeByLoginId/{userId}";
//
//    //根据识别码id获取识别码
//    public static final String GET_GETIDENTIFICATIONCODEBYIDENTIFICATIONCODEID = "/auth_OpenApi/getIdentificationCodeByIdentificationCodeId/{identificationCodeId}/{identificationCode}";
//
//    //树形获取组织机构用户
//    public static final String GET_ORGANDUSERSWITHTREE = "/auth_OpenApi/getOrgAndUsersWithTree/{resourceTypeId}";
//
//    //保存消息接口
//    public static final String SAVE_MESSAGE = "/auth_OpenApi/saveMessage";
//
//    //查询消息接口
//    public static final String QUERY_MESSAGE = "/auth_OpenApi/queryMessages";
//
//    //删除消息接口
//    public static final String DELETE_MESSAGE = "/auth_OpenApi/deleteMessage/{id}";
//
//    //查询机构和应用系统
//    public static final  String QUERY_RESOURCE_ROOT = "/auth_OpenApi/getResourceByResourceType/{resourceType}";
//
//    //查询机构和应用系统
//    public static final  String QUERY_SUBORDINATE_ORGAN = "/auth_OpenApi/getSubordinateOrganizations/{parentId}";
//
//    //查询组织机构下所有的人员成员(不分页)
//    public static final String GET_USERS_BY_DEPARTMENT_ID = "/auth_OpenApi/getUsersByDepartmentId/{departmentId}";
//
//    //根据登录名查询用户的所有角色的id及资源名称
//    public  static final String GET_ROLE_ID_BY_LOGIN_NAME = "/auth_OpenApi/getRoleIdAndNameByLoginName/{loginName}";
//
//    //查询组织机构下所有的人和组织机构
//    public static final String GET_ORGANS_USERS = "/auth_OpenApi/getSubOrgan/{parentId}";
//
//    //获取系统配置
//    public static final String GET_SYS_PARAM = "/auth_OpenApi/getSystemParameters";

}
