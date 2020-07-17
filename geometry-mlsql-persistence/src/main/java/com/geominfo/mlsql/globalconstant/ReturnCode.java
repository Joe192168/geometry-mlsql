package com.geominfo.mlsql.globalconstant;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: ReturnCode
 * @author: anan
 * @create: 2020-07-14 09:46
 * @version: 1.0.0
 */
public class ReturnCode {
    public static String TEAM_EXISTS = "Team exists";
    public static String TEAM_NOT_EXISTS = "Team not exists";
    public static String TEAM_ROLE_NOT_EXISTS = "Team relation role not exists";
    public static String TEAM_OR_USER_NOT_EXISTS = "Team or user not exists";
    public static String USER_NOT_EXISTS = "User not exists";
    public static String USER_EXISTS = "User exists";
    public static String SUCCESS = "success";
    public static Integer UserInvited = 1;
    public static Integer UserConfirmed = 2;
    public static Integer UserRefused = 3;
    public static Integer UserOwner = 4;
    public static Integer RETURN_SUCCESS_STATUS = 200;
    public static Integer RETURN_ERROR_STATUS = 400;
    public static String SCRIPT_FILE_NO_EXISTS = "file not exists";
}
