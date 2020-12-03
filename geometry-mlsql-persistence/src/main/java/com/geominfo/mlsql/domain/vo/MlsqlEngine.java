package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlEngine
 * @author: anan
 * @create: 2020-11-23 14:17
 * @version: 1.0.0
 */
@Data
public class MlsqlEngine {
    private long id;
    private String name;
    private String url;
    private String home;
    private String consoleUrl;
    private String fileServerUrl;
    private String authServerUrl;
    private int skipAuth;
    private String extraOpts;
    private String accessToken;

    public MlsqlEngine(long id, String name, String url, String home, String consoleUrl,
                       String fileServerUrl, String authServerUrl, int skipAuth, String extraOpts, String accessToken) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.home = home;
        this.consoleUrl = consoleUrl;
        this.fileServerUrl = fileServerUrl;
        this.authServerUrl = authServerUrl;
        this.skipAuth = skipAuth;
        this.extraOpts = extraOpts;
        this.accessToken = accessToken;
    }
}
