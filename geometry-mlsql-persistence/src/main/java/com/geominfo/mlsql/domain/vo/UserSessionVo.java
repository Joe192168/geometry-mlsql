package com.geominfo.mlsql.domain.vo;

import com.geominfo.authing.common.enums.EnumApplicationResource;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description: 用户会话信息实体
 * @author: LF
 * @create: 2020/11/24 16:11
 * @version: 1.0.0
 */
public class UserSessionVo {
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 用户名
     */
    private String userName;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 浏览器版本号
     */
    private String browserVersion;
    /**
     * 会话内容
     */
    private String accessToken;

    private BigDecimal resourceId;

    private String tokenKey;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public BigDecimal getResourceId() {
        return resourceId = new BigDecimal(EnumApplicationResource.MLSQL.getResourceId());
    }

    public void setResourceId(BigDecimal resourceId) {
        this.resourceId = resourceId;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    @Override
    public String toString() {
        return "UserSessionVo{" +
                "loginName='" + loginName + '\'' +
                ", userName='" + userName + '\'' +
                ", ip='" + ip + '\'' +
                ", browserVersion='" + browserVersion + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", resourceId=" + resourceId +
                ", tokenKey='" + tokenKey + '\'' +
                '}';
    }
}
