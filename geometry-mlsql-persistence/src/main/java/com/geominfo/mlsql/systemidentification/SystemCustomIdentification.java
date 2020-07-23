package com.geominfo.mlsql.systemidentification;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description: 本系统中所有自定义标识
 * @author: LF
 * @create: 2019/12/2
 * @version: 1.0.0
 */
public class SystemCustomIdentification {

    //所有接口返回结果标识
    public static final String FLAG = "flag";

    //接口返回信息标识
    public static final String MSG = "msg";

    //接口返回数据标识
    public static final String DATA = "data";

    //资源ID
    public static final String ID = "ID";

    //本系统中的jwt
    public static final String JWT = "jwt";

    //用户的JWT的前缀
    public static final String JWT_SESSION = "JWT-SESSION-";

    //系统中token的标识
    public static final String TOKEN_SERVER = "token-server";

    //人员信息标识
    public static final String USER = "user";

    //本系统中用户权限树
    public static final String USER_PERMISSION_TREES = "allPermissionTrees";

    //数据集默认文件夹名称
    public static final String DATA_SET_FILE_NAME = "我的数据集";

    //默认空间名称
    public static  final String DEFAULT_SPACE_NAME = "默认空间";

    //个人空间名称
    public static final String PERSONAL_SPACE_NAME = "个人空间";

    //探索空间名称
    public static final String  EXPLORE_NAME = "探索空间";

    //数据集的菜单标识/空间权限标识
    public static final String DATA_SET_MENU_ID = "5";

    //工作空间成员默认角色
    public static final String WORKSPACE_MEMBERS_ROLES = "wsm_kjgly";

    //资源收藏或取消的标识
    public static final String COLLECTION_OR_CANCEL= "collectionOrCancel";

    //资源收藏的标识
    public static final String RESOURCE_COLLECTION = "collection";

    //资源取消收藏的标识
    public static final String RESOURCE_CANCEL = "cancel";

    //资源类型标识
    public static final String RESOURCE_TYPE = "resourcesType";

    //文件夹目录标识
    public static final String FOLDER_TREE = "folderTree";

    //当前树的ID
    public static final String TREE_ID = "id";

    //当前树的名称
    public static final String TREE_NAME = "resourceName";

    //当前树的父节点ID
    public static final String TREE_PARENT_ID = "parentId";

    //电子表格编辑成功后返回标识
    public static final String SPREAD_SHEETS = "SpreadSheet";

    //获取后台时间的标识
    public static final String SYSTEM_TIME = "time";

    //系统中默认值 int型
    public static final int DEFAULT_INT_VALUE = 1;

    //资源默认值0
    public static final BigDecimal DEFAULT_0 = BigDecimal.valueOf(0) ;

    //资源默认值1
    public static final BigDecimal DEFAULT_1 = BigDecimal.valueOf(1) ;

    //默认工作空间的标识
    public static final String DEFAULT_WORKSPACE_VAL = "0";

    //个人工作空间的标识
    public static final String PERSONAL_WORKSPACE_VAL = "1";

    //普通工作空间的标识
    public static final String WORKSPACE_VAL = "2";

    //数据门户
    public static final String  SJMH = "1";

    //指标看板
    public static final String  ZZKB = "2";

    //电子表格
    public static final String  DZBG = "3";

    //图表分析
    public static final String  TB = "4";

    //数据集
    public static final String  DS = "5";

    //数据源
    public static final String  SJY = "6";

    //自助报表
    public static final String  ZZBB = "7";

    //用户的工作空间集合（除个人空间）
    public static final String WORKSPACE_LIST = "workspaceList";

    //用户的工作空间（分页）
    public static final String WORKSPACE_PAGE = "pageInfo";

    //登录时获取用户登录名的标识
    public static final String LOGIN_NAME = "appId";

    public static final String  PRODUCT_ID = "productId";

    //个人工作空间
    public static final String PERSONAL_WORKSPACE = "personalWorkspaceInfo";

    //工作空间成员
    public static final String WORKSPACE_MEMBERS = "workSpaceMemberInfos";

    //查询用户的空间成员
    public static final String SPACE_MEMBERS = "spaceMembers";

    //查询空间成员
    public static final String QUERY_SPACE_MEMBERS = "workSpaceMembers";

    //查询空间和角色
    public static final String SPACE_ROLES = "spaceAndRoles";

    //查询工作空间
    public static final String WORKSPACES = "allWorkSpace";

    //资源集合
    public static final String RESOURCES_LISTS = "resourcesLists";

    //我的资源
    public static final String MY_RESOURCES = "myResources";

    //全部的资源
    public static final String ALL_RESOURCES = "allResources";

    //收藏资源
    public static final String SYSTEM_RESOURCES = "systemResources";

    //系统资源集
    public static final String SYSTEM_RESOURCES_LIST = "systemResourcesList";

    //关联资源
    public static final String RELATION_RESOURCES = "relationResources";

    //组织机构
    public static final String ORGANIZATIONS = "organizations";

    //查询工作空间
    public static final String WORK_SPACES = "workspaces";

    //人员、岗位
    public static final String USER_JOB = "usesOrJosLists";

    //部门
    public static final String DEPARTMENTS = "departments";

    //人员资源
    public static final String USER_RESOURCES = "userResources";

    //接口操作日志名称
    public static final String LOG_NAME = "业务操作日志";



}
