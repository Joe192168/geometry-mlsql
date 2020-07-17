package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlGroupUser
 * @author: anan
 * @create: 2020-07-09 16:19
 * @version: 1.0.0
 */
@Data
public class MlsqlGroupUser {
    private int id;
    private int groupId;
    private int userId;
    private int status;
    private MlsqlUser mlsqlUser;
    private MlsqlGroup mlsqlGroup;
    public static Integer invited = 1;
    public static Integer confirmed = 2;
    public static Integer refused = 3;
    public static Integer owner = 4;
}
