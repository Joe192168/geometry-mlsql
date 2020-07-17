package com.geominfo.mlsql.domain.vo;

import lombok.Data;
import org.apache.logging.log4j.core.script.ScriptFile;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: ScriptUserRw
 * @author: anan
 * @create: 2020-07-15 18:44
 * @version: 1.0.0
 */
@Data
public class ScriptUserRw {
    private Integer id;
    private int fileId;
    private int userId;
    private Integer readable=1;
    private Integer writable=1;
    private Integer isOwner=1;
    private Integer isDelete = 2;
    private MlsqlUser mlsqlUser;
    private ScriptFile scriptFile;

    public static Integer Delete = 1;
    public static Integer UnDelete = 2;

    public static Integer READ = 1;
    public static Integer UNREAD = 2;

    public static Integer WRITE = 1;
    public static Integer UNWRITE = 2;

    public static Integer OWNER = 1;
    public static Integer UNOWNER = 2;
}
