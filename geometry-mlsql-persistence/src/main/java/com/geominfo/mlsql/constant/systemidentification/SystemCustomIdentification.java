package com.geominfo.mlsql.constant.systemidentification;

/**
 * @program: geometry-bi
 * @description: 本系统中所有自定义标识
 * @author: LF
 * @create: 2019/12/3
 * @version: 1.0.0
 */
public class SystemCustomIdentification {

    //所有接口返回结果标识
    public static final String FLAG = "flag";

    //接口返回信息标识
    public static final String MSG = "msg";

    //接口返回数据标识
    public static final String DATA = "data";

    //权限数据返回数据标识（三方应用请求查询权限数据）
    public static final String PERMISSION_DATA = "PermissionData";

    //第三方平台数据标识，查询数据（资源表数据）get请求方式
    public static final String OPEN_QUERY_DATA = "openQueryData";

    //第三方平台数据标识，默认返回标识(资源表数据)
    public static final String OPEN_DATA = "openData";

    //请求接口的路径标识
    public static final String URL = "url";

    //请求接口的方法类型的标识
    public static final String METHOD = "method";

    //消息附件文件路径
    public static final String MESSAGE_FILE_URL = "/messageFiles/";

    //权限系统中用的分隔符 ","
    public static final String SEPARATOR = ",";

    //系统中用到的连接符 "-"
    public static final String CONNECTOR = "-";

    //插件类型信息,默认为2, 2:资源插件
    public static final int  PLUGIN_TYPE_VALUE = 2;

    //系统中默认账户
    public static final String DEFAULT_USER = "admin";

    //系统中字符串默认值
    public static final String DEFAULT_VALUE = "0";

    //系统中字符串默认值_01
    public static final String DEFAULT_VALUE_S01 = "1";

    //系统中字符串默认值_02
    public static final String DEFAULT_VALUE_S02 = "2";

    //系统中整型默认值_01
    public static final Integer DEFAULT_VALUE_I01 = 0;

    //系统中整型默认值_02
    public static final Integer DEFAULT_VALUE_I02 = 1;

    //当前树的ID
    public static final String TREE_ID = "id";

    //当前树的名称
    public static final String TREE_NAME = "resourceName";

    public static final String TREE_TYPE = "resourceTypeId";

    //当前树的父节点ID
    public static final String TREE_PARENT_ID = "parentId";

    //前端传默认值
    public static final String DEFAULT_UI_VALUE = "null";

    //用户没有权限标识
    public static final String USER_NO_PERMISSION = "NO";

    //用户名称
    public static final String NAME = "name";

    //用户名标识
    public static final String IS_NAME = "isName";

    //密码标识
    public static final String PASS_WORD = "passWord";

    //新用户默认密码
    public static final String DEFAULT_PASS_WORD = "123456";

    //更新数据库请求接口秘钥
    public static final String INTERFACE_PRIVATE_KEY = "12EDFD7D94D2D67FA108FED214365893";

    //系统资源ID
    public static final String RESOURCE_ID = "resourceId";

    //系统资源
    public static final String SYSTEM_RESOURCES = "systemResources";

    //可授权的资源（应用程序）的标识
    public static final String AUTHORIZE_RESOURCES = "authorizeResources";

    //资源操作配置
    public static final String PLUGIN_OPERATE_INFO = "pluginOperateInfo";

    //可授权资源类型
    public static final String AUTHORIZE_RESOURCE_TYPE = "resourceTypes";

    //组织机构扩展属性
    public static final String OFFICE_PROPERTY = "officeProperty";

    //岗位数据
    public static final String JOB_RESOURCE = "job";

    //组织成员角色标识
    public static final String ORGANIZATION_MEMBER_ROLE = "roleMemberOrganizations";

    //角色下成员
    public static final String MEMBER_INFO = "memberInfo";

    //人员信息
    public static final String USERS_INFO = "users";

    //人员扩展信息
    public static final String USER_PROPERTY = "userProperty";

    //查询用户的权限标识
    public static final String APP_PERMISSION = "appAllRightInfo";

    //角色id和资源名称
    public static final String ROLE_ID_RESOURCE_NAME = "userRoleIdAndNames";

    //查询统一认证平台权限
    public static final String ALL_PERMISSION = "allPermissions";

    //人员所有的成员
    public static final String USER_MEMBER = "userMembers";

    //查询组织权限
    public static final String ORGAN_PERMISSION = "rightInfos";

    //模糊查询资源
    public static final String ALL_RESOURCE = "allResources";

    //组织信息
    public static final String ORGAN_RESOURCE = "organizations";

    //人员岗位
    public static final String USER_JOB = "usesOrJosLists";

    //部门
    public static final String DEPARTMENTS = "departments";

    //可入职的岗位
    public static final String JOBS_RESOURCES = "jobsResources";

    //资源类型
    public static final String RESOURCE_TYPE_LIST = "resourceTypeLists";

    //系统账号
    public static final String SYSTEM_ACCOUNT = "SystemAccount";

    //人员资源
    public static final String USER_RESOURCE = "userResources";

    //管理职务
    public static final String MANAGEMENT_ROLES = "managementRoles";

    //编制类别
    public static final String STAFF_TYPE_IDS = "staffTypeIds";

    //人员身份
    public static final String USER_IDENTITIES = "userIdentities";

    //手术级别
    public static final String OPERATION_LEVELS = "operationLevels";

    //专业信息
    public static final String MAJOR_INFO = "forMajorsInfo";

    //学位
    public static final String DEGREES_INFO = "degreesInfo";

    //学历
    public static final String DIPLOMAS_INFO = "diplomasInfo";

    //性别
    public static final String SEX_INFO = "sexInfos";

    //可选户口
    public static final String HOUSE_HOLD_INFO = "householdPropertyInfos";

    //所学专业
    public static final String STUDY_MAJORS = "studyMajorsInfo";

    //政治面貌
    public static final String POLITICAL_STATUS = "politicalStatusesInfo";

    //残疾等级
    public static final String DISABILITY_LEVELS = "disabilityLevelsInfo";

    //残疾类别
    public static final String DISABILITY_TYPE = "disabilityTypesInfo";

    //民族
    public static final String NATION_INFO = "nationsInfos";

    //血型
    public static final String BLOOD_TYPE = "bloodTypesinfos";

    //证件类型
    public static final String CREDENTIALS = "Credentials";

    //行政区
    public static final String REGIONALISM_INFO = "RegionalismInfos";



}
