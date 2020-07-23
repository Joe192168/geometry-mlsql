package com.geominfo.mlsql.systemidentification;

/**
 * @program: geometry-bi
 * @description: 接口返回信息
 * @author: LF
 * @create: 2019/11/29
 * @version: 1.0.0
 */
public class InterfaceReturnInformation {
    public static String TEAM_EXISTS = "Team exists";

    public static String TEAM_NOT_EXISTS = "Team not exists";

    public static String TEAM_ROLE_NOT_EXISTS = "Team relation role not exists";

    public static String TEAM_OR_USER_NOT_EXISTS = "Team or user not exists";

    public static String USER_NOT_EXISTS = "User not exists";

    public static String USER_EXISTS = "User exists";

    public static String SUCCESS = "success";

    public static String SCRIPT_FILE_NO_EXISTS = "file not exists";

    public static final String LOGIN_SUCCESS = "登录成功!";

    public static final String SAVE_SUCCESS = "保存数据成功!";

    public static final String SAVE_FAIL = "保存数据失败!";

    public static final String SAVE_ERROR = "保存数据时发生异常，请稍后重试！";

    public static final String UPDATE_SUCCESS = "修改数据成功!";

    public static final String UPDATE_FAIL = "修改数据失败!";

    public static final String UPDATE_ERROR = "修改数据时发生异常，请稍后重试！";

    public static final String DELETE_SUCCESS = "删除成功!";

    public static final String DELETE_FAIL = "删除失败!";

    public static final String DELETE_ERROR = "删除数据时发生异常，请稍后重试！";

    public static final String QUERY_SUCCESS = "查询成功！";

    public static final String QUERY_FAIL = "查询失败！";

    public static final String QUERY_ERROR = "查询时发生异常，请稍后重试！";

    public static final String NAME_REPEAT = "命名重复！";

    public static final String FILE_EXIST_FILE = "该文件夹下存在级联文件,不可删除,请先删除文件下的文件!";

    public static final String RESOURCE_EXIST_RESOURCE = "该资源存在有关联数据,不可删除,请先删除关联数据！";

    public static final String ATTRIBUTE_SAVE_SUCCESS = "保存属性成功!";

    public static final String ATTRIBUTE_SAVE_FAIL = "保存属性失败!";

    public static final String ATTRIBUTE_SAVE_ERROR = "保存资源属性时发生异常，请稍后重试!";

    public static final String FOLDER_IS_EMPTY = "文件夹文件名为空!";

    public static final String FOLDER_SAVE_SUCCESS = "新建文件夹成功！";

    public static final String FOLDER_SAVE_FAIL = "新建文件夹失败！";

    public static final String FOLDER_SAVE_ERROR = "新建文件夹时发生异常，请稍后重试!";

    public static final String FOLDER_UPDATE_SUCCESS = "编辑文件夹成功！";

    public static final String FOLDER_UPDATE_FAIL = "编辑文件夹失败！";

    public static final String FOLDER_UPDATE_ERROR = "编辑文件夹时发生异常，请稍后重试！";

    public static final String FOLDER_TREE_ERROR = "获取目录时发生异常，请稍后重试";

    public static final String ADD_COLLECTION_SUCCESS = "添加收藏成功!";

    public static final String ADD_COLLECTION_FAIL = "添加收藏失败!";

    public static final String CANCEL_COLLECTION_SUCCESS = "取消收藏成功!";

    public static final String CANCEL_COLLECTION_FAIL = "取消收藏失败!";

    public static final String RESOURCE_MOVE_SAVE = "移动资源成功！";

    public static final String RESOURCE_MOVE_FAIL = "移动资源失败！";

    public static final String RESOURCE_MOVE_ERROR = "移动资源时发生异常，请稍后重试！";

    public static final String SPREAD_SHEET_ERROR = "编辑电子表格发生异常，请稍后重试！";

    public static final String SYSTEM_TIME_MSG = "获取后台系统时间成功!";

    public static final String SET_WORKSPACE_ERROR = "设置默认空间时发生异常，请稍后重试！";

    public static final String DEFAULT_WORKSPACE_EXIST = "此空间为默认空间，不可重复设置！";

    public static final String SET_WORKSPACE_SUCCESS = "设置默认工作空间成功!";

    public static final String SET_WORKSPACE_FAIL = "设置默认工作空间失败!";

    public static final String TRAN_WORKSPACE_SUCCESS = "转让工作空间成功!";

    public static final String TRAN_WORKSPACE_FAIL = "转让工作空间失败！";

    public static final String TRAN_WORKSPACE_ERROR = "转让工作空间时发生异常,请稍后再试！";

    public static final String SPACE_MEMBER_SAVE_SUCCESS = "新增工作空间成员成功!";

    public static final String SPACE_MEMBER_SAVE_FAIL = "此账户已在其他组织中！";

    public static final String SPACE_MEMBER_DELETE_SUCCESS = "删除工作空间成员成功!";

    public static final String SPACE_MEMBER_DELETE_FAIL = "此工作空间成员信息不存在！";

    public static final String SPACE_MEMBER_SAVE_ERROR = "保存工作空间成员信息时发生异常，请稍后重试！";

    public static final String QUERY_SPACE_MEMBER_ERROR = "查询用户当前空间下所有的工作空间成员时发生异常，请稍后重试！";

    public static final String QUERY_SPACE_ROLES_ERROR = "根据用户id，查询用户所属的工作空间及所属空间角色时发生异常，请稍后重试！";

    public static final String INIT_WORKSPACE_ERROR = "初始化工作空间发生异常，请稍后再试！";

    public static final String RELATION_RESOURCES_ERROR = "查询关联资源发生异常，请稍后重试！";

    public static final String QUERY_ORGANIZATIONS_ERROR = "查询组织机构树时发生异常，请稍后重试！";

    public static final String QUERY_USER_ERROR = "获取用户信息时发生异常，请稍后重试！";

    public static final String ADD_WORKSPACE_FAIL = "此工作空间已存在!";

    public static final String WORKSPACE_EXIST_DATA = "该工作空间下已经创建数据";

    public static final String FEATURE_REPORT_ERROR = "保存自助报表时发生异常，请稍后再试！";

    public static final String SPACE_RESOURCE_FAIL = "保存资源失败，资源类型不能为空！";

    public static final String SAVE_DATA_PRODUCT_ERROR = "保存数据门户时发生异常，请稍后重试！";

    public static final String NAME_ISNULL = "名称不能为空！";

    public static final String SHARE_SUCCESS = "分享资源成功！";

    public static final String SHARE_ERROR = "分享资源时发生异常，请稍后再试！";

    public static final String SHARE_PARAM_ERROR = "分享资源，分享结束时间不能为空！";

    public static final String ALIAS_OCCUPY = "数据门户别名已被占用！";

    public static final String INTERFACE_LOG_SUCCESS = "新增接口日志成功！";

    public static final String INTERFACE_LOG_ERROR = "新增接口日志发生异常，请稍后再试！";

}
