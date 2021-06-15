package com.geominfo.mlsql.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: geometry-bi
 * @description: 工作空间信息
 * @author: LF
 * @create: 2021/6/8 17:49
 * @version: 1.0.0
 */
public class WorkSpaceInfoVo {

    private String spaceId;

    private String workSpaceName;

    private BigDecimal spaceOwnerId;

    private String ownerName;

    private BigDecimal spaceMemberId;

    private String state;

    private String describe;

    private Date createTime;

    private Date updateTime;

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getWorkSpaceName() {
        return workSpaceName;
    }

    public void setWorkSpaceName(String workSpaceName) {
        this.workSpaceName = workSpaceName;
    }

    public BigDecimal getSpaceOwnerId() {
        return spaceOwnerId;
    }

    public void setSpaceOwnerId(BigDecimal spaceOwnerId) {
        this.spaceOwnerId = spaceOwnerId;
    }

    public BigDecimal getSpaceMemberId() {
        return spaceMemberId;
    }

    public void setSpaceMemberId(BigDecimal spaceMemberId) {
        this.spaceMemberId = spaceMemberId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    @Override
    public String toString() {
        return "WorkSpaceInfoVo{" +
                "spaceId='" + spaceId + '\'' +
                ", workSpaceName='" + workSpaceName + '\'' +
                ", spaceOwnerId=" + spaceOwnerId +
                ", ownerName='" + ownerName + '\'' +
                ", spaceMemberId=" + spaceMemberId +
                ", state='" + state + '\'' +
                ", describe='" + describe + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
