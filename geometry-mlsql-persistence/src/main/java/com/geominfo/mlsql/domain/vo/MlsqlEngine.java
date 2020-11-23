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
    private int id;
    private String name;
    private String url;
    private String home;
    private String consoleUrl;
    private String fileServerUrl;
    private String authServerUrl;
    private int skipAuth;
    private String extraOpts;
    private String accessToken;
}
