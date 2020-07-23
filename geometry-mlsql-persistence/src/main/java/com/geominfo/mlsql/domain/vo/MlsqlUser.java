package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlUser
 * @author: anan
 * @create: 2020-06-03 12:00
 * @version: 1.0.0
 */
@Data
public class MlsqlUser implements Serializable{
    public static String STATUS_LOCK = "lock";
    public static String STATUS_NORMAL = "normal"; // null or normal means ok
    public static String STATUS_PAUSE = "pause"; // shutdown all write/update function
    private int id;
    private String name;
    private String password;
    private String backendTags;
    private String role;
    private String status;
    private List<MlsqlGroupUser> mlsqlGroupUsers;
    private List<MlsqlRoleMember> mlsqlRoleMembers;
    private List<ScriptUserRw> scriptUserRws;

}
