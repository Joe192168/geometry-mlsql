package com.geominfo.mlsql.shiro.token;

import com.geominfo.mlsql.utils.AESUtil;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @program: geometry-bi
 * @description: Password token
 * @author: 肖乔辉
 * @create: 2018-08-17 15:39
 * @version: 1.0.0
 */
public class PasswordToken implements AuthenticationToken{

    private static final long serialVersionUID = 1L;
    private String appId;
    private String password;
    private String timestamp;
    private String host;
    private String tokenKey;

    public PasswordToken(String appId, String password, String timestamp, String host,String tokenKey) throws Exception {
        this.appId = appId;
        this.timestamp = timestamp;
        this.host = host;
        this.password = AESUtil.aesDecode(password,tokenKey);
        this.tokenKey = tokenKey;

    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return this.password;
    }

    public Object getPrincipal() {
        return this.appId;
    }

    public Object getCredentials() {
        return this.password;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}
