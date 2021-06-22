package com.geominfo.mlsql.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @title: MlsqlExecuteSqlVO
 * @date 2021/4/6 15:10
 */
@Data
@ApiModel("MlsqlExecuteSqlVO")
public class MlsqlExecuteSqlVO {

    @ApiModelProperty(name = "sql",value = "需要执行的MLSQL内容",required = true)
    private String sql;

    @ApiModelProperty(name = "owner",value = "当前发起请求的租户",required = true)
    private String owner;

    @ApiModelProperty(name = "jobType",value = "任务类型 ")
    private String jobType = "script";

    @ApiModelProperty(name = "executeMode",value = "如果是执行MLSQL则为query,如果是为了解析MLSQL则为analyze。很多插件会提供对应的executeMode从而使得用户可以通过HTTP接口访问插件功能")
    private String executeMode = "query";

    @ApiModelProperty(name = "jobName",value = "任务名称，一般用uuid，方便查看任务进度条和日志，前端生成",required = true)
    private String jobName;

    @ApiModelProperty(name = "timeout",value = "任务执行的超时时间")
    private int timeout = -1;

    @ApiModelProperty(name = "silence",value = "是否输出结果")
    private Boolean silence = false;

    @ApiModelProperty(name = "sessionPerUser",value = "按用户创建sesison")
    private Boolean sessionPerUser = false;

    @ApiModelProperty(name = "async",value = "请求是不是异步执行")
    private Boolean async = false;

    @ApiModelProperty(name = "callback",value = "与async配合使用，当async为true时，需要设置回调url")
    private String callback;

    @ApiModelProperty(name = "skipInclude",value = "禁止使用include语法")
    private Boolean skipInclude = false;

    @ApiModelProperty(name = "skipAuth",value = "禁止权限验证")
    private Boolean skipAuth = true;

    @ApiModelProperty(name = "skipGrammarValidate",value = "跳过语法验证")
    private Boolean skipGrammarValidate = true;

    @ApiModelProperty(name = "includeSchema",value = "返回的结果是否包含单独的schema信息")
    private Boolean includeSchema = false;

    @ApiModelProperty(name = "fetchType",value = "take/collect, take在查看表数据的时候非常快")
    private String fetchType = "collect";

    @ApiModelProperty(name = "defaultPathPrefix",value = "工作空间主目录的基础目录",required = true)
    private String defaultPathPrefix;

    @ApiModelProperty(name = "defaultIncludeFetchUrl",value = "Engine获取include脚本的地址",required = true)
    private String defaultIncludeFetchUrl;

    @ApiModelProperty(name = "defaultConsoleUrl",value = "console地址",required = true)
    private String defaultConsoleUrl;

    @ApiModelProperty(name = "defaultFileserverUrl",value = "下载文件服务器地址，一般默认也是console地址")
    private String defaultFileserverUrl;

    @ApiModelProperty(name = "defaultFileserverUploadUrl",value = "上传文件服务器地址，一般默认也是console地址")
    private String defaultFileserverUploadUrl;

    @ApiModelProperty(name = "authClient",value = "数据访问客户端的class类")
    private String authClient;

    @ApiModelProperty(name = "authServerUrl",value = "数据访问验证服务器地址")
    private String authServerUrl;

    @ApiModelProperty(name = "authSecret",value = "engine回访请求服务器的秘钥。比如console调用了engine，需要传递这个参数， 然后engine要回调console,那么需要将这个参数带回")
    private String authSecret;

    private Boolean refuseConnect;
}
