package com.geominfo.mlsql.domain.result;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: geometry-bi
 * @description: 被分享的脚本信息
 * @author: LF
 * @create: 2021/7/1 11:48
 * @version: 1.0.0
 */
public class SharedInfoResult {

    private BigDecimal id;
    //分享人id
    private BigDecimal shareId;

    //分享人名称
    private String shareName;

    //分享时间
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date shareTime;

    //分享备注
    private String shareRemark;

    //分享资源id
    private BigDecimal resourceId;

    //分享资源名称
    private String resourceName;

    //分享结束时间
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date shareEndTime;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getShareId() {
        return shareId;
    }

    public void setShareId(BigDecimal shareId) {
        this.shareId = shareId;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public Date getShareTime() {
        return shareTime;
    }

    public void setShareTime(Date shareTime) {
        this.shareTime = shareTime;
    }

    public String getShareRemark() {
        return shareRemark;
    }

    public void setShareRemark(String shareRemark) {
        this.shareRemark = shareRemark;
    }

    public BigDecimal getResourceId() {
        return resourceId;
    }

    public void setResourceId(BigDecimal resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Date getShareEndTime() {
        return shareEndTime;
    }

    public void setShareEndTime(Date shareEndTime) {
        this.shareEndTime = shareEndTime;
    }

    @Override
    public String toString() {
        return "SharedInfoResult{" +
                "shareId=" + shareId +
                ", shareName='" + shareName + '\'' +
                ", shareTime=" + shareTime +
                ", shareRemark='" + shareRemark + '\'' +
                ", resourceId=" + resourceId +
                ", resourceName='" + resourceName + '\'' +
                ", shareEndTime=" + shareEndTime +
                '}';
    }
}
