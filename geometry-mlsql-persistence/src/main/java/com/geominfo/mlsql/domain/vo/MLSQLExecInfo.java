package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MLSQLExecInfo
 * @author: anan
 * @create: 2020-06-11 14:34
 * @version: 1.0.0
 */
public class MLSQLExecInfo implements Serializable{
    private String sql;
    private String owner = "admin";
    private String jobType = "script";

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getExecuteMode() {
        return executeMode;
    }

    public void setExecuteMode(String executeMode) {
        this.executeMode = executeMode;
    }

    public String getSilence() {
        return silence;
    }

    public void setSilence(String silence) {
        this.silence = silence;
    }

    public String getSessionPerUser() {
        return sessionPerUser;
    }

    public void setSessionPerUser(String sessionPerUser) {
        this.sessionPerUser = sessionPerUser;
    }

    public String getAsync() {
        return async;
    }

    public void setAsync(String async) {
        this.async = async;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getSkipInclude() {
        return skipInclude;
    }

    public void setSkipInclude(String skipInclude) {
        this.skipInclude = skipInclude;
    }

    public String getSkipAuth() {
        return skipAuth;
    }

    public void setSkipAuth(String skipAuth) {
        this.skipAuth = skipAuth;
    }

    public String getSkipGrammarValidate() {
        return skipGrammarValidate;
    }

    public void setSkipGrammarValidate(String skipGrammarValidate) {
        this.skipGrammarValidate = skipGrammarValidate;
    }

    public String getAuth_client() {
        return auth_client;
    }

    public void setAuth_client(String auth_client) {
        this.auth_client = auth_client;
    }

    public String getAuth_server_url() {
        return auth_server_url;
    }

    public void setAuth_server_url(String auth_server_url) {
        this.auth_server_url = auth_server_url;
    }

    public String getAuth_secret() {
        return auth_secret;
    }

    public void setAuth_secret(String auth_secret) {
        this.auth_secret = auth_secret;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    private String executeMode = "query";
    private String silence = "false";
    private String sessionPerUser = "false";
    private String async = "false";
    private String callback = "false";
    private String skipInclude = "false";
    private String skipAuth = "true";
    private String skipGrammarValidate = "true";
    private String auth_client;
    private String auth_server_url;
    private String auth_secret;
    private String tags;
}
