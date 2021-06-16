package com.geominfo.mlsql.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/16 10:57
 * @version: 1.0.0
 */
@TableName("t_engine_info")
public class EngineInfo {

    @TableId(type = IdType.INPUT)
    private BigDecimal id;

    //引擎名称
    private String engineName;

    //引擎uri
    private String engineUri;

    //主目录前缀
    private String homePrefix;

    //结果返回uri
    private String resultUri;

    //文件服务uri
    private String fileServiceUri;

    //权限服务uri
    private String authServiceUri;

    //是否跳过权限
    private BigDecimal skipAuth;

    //其他配置
    private String extraOpts;

    //操作员id
    private Integer operatorId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    //记录状态
    private BigDecimal recordState;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public String getEngineUri() {
        return engineUri;
    }

    public void setEngineUri(String engineUri) {
        this.engineUri = engineUri;
    }

    public String getHomePrefix() {
        return homePrefix;
    }

    public void setHomePrefix(String homePrefix) {
        this.homePrefix = homePrefix;
    }

    public String getResultUri() {
        return resultUri;
    }

    public void setResultUri(String resultUri) {
        this.resultUri = resultUri;
    }

    public String getFileServiceUri() {
        return fileServiceUri;
    }

    public void setFileServiceUri(String fileServiceUri) {
        this.fileServiceUri = fileServiceUri;
    }

    public String getAuthServiceUri() {
        return authServiceUri;
    }

    public void setAuthServiceUri(String authServiceUri) {
        this.authServiceUri = authServiceUri;
    }

    public BigDecimal getSkipAuth() {
        return skipAuth;
    }

    public void setSkipAuth(BigDecimal skipAuth) {
        this.skipAuth = skipAuth;
    }

    public String getExtraOpts() {
        return extraOpts;
    }

    public void setExtraOpts(String extraOpts) {
        this.extraOpts = extraOpts;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getRecordState() {
        return recordState;
    }

    public void setRecordState(BigDecimal recordState) {
        this.recordState = recordState;
    }

    @Override
    public String toString() {
        return "EngineInfo{" +
                "id=" + id +
                ", engineName='" + engineName + '\'' +
                ", engineUri='" + engineUri + '\'' +
                ", homePrefix='" + homePrefix + '\'' +
                ", resultUri='" + resultUri + '\'' +
                ", fileServiceUri='" + fileServiceUri + '\'' +
                ", authServiceUri='" + authServiceUri + '\'' +
                ", skipAuth=" + skipAuth +
                ", extraOpts='" + extraOpts + '\'' +
                ", operatorId=" + operatorId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", recordState=" + recordState +
                '}';
    }
}
