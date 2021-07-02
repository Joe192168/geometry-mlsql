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
 * @create: 2021/7/1 10:17
 * @version: 1.0.0
 */
@TableName("t_share_info")
public class ShareInfo {

    @TableId(type = IdType.INPUT)
    private BigDecimal id;

    //资源id
    private BigDecimal resourceId;

    //被分享人id
    private BigDecimal sharedId;

    //分享结束时间
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date shareEndTime;

    //分享人id
    private BigDecimal sharerId;

    //分享备注
    private String shareRemark;

    //扩展信息
    private String extraOpts;

    //操作时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date operatorTime;

    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    //分享状态
    private BigDecimal shareState;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getResourceId() {
        return resourceId;
    }

    public void setResourceId(BigDecimal resourceId) {
        this.resourceId = resourceId;
    }

    public BigDecimal getSharedId() {
        return sharedId;
    }

    public void setSharedId(BigDecimal sharedId) {
        this.sharedId = sharedId;
    }

    public BigDecimal getSharerId() {
        return sharerId;
    }

    public void setSharerId(BigDecimal sharerId) {
        this.sharerId = sharerId;
    }

    public Date getShareEndTime() {
        return shareEndTime;
    }

    public void setShareEndTime(Date shareEndTime) {
        this.shareEndTime = shareEndTime;
    }

    public String getShareRemark() {
        return shareRemark;
    }

    public void setShareRemark(String shareRemark) {
        this.shareRemark = shareRemark;
    }

    public String getExtraOpts() {
        return extraOpts;
    }

    public void setExtraOpts(String extraOpts) {
        this.extraOpts = extraOpts;
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getShareState() {
        return shareState;
    }

    public void setShareState(BigDecimal shareState) {
        this.shareState = shareState;
    }

    @Override
    public String toString() {
        return "ShareInfo{" +
                "id=" + id +
                ", resourceId=" + resourceId +
                ", sharedId=" + sharedId +
                ", sharerId=" + sharerId +
                ", shareRemark='" + shareRemark + '\'' +
                ", extraOpts='" + extraOpts + '\'' +
                ", operatorTime=" + operatorTime +
                ", updateTime=" + updateTime +
                ", shareState=" + shareState +
                '}';
    }
}
