package com.geominfo.mlsql.domain.result;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/23 11:15
 * @version: 1.0.0
 */
public class WorkSpaceInfoResult {

    private BigDecimal spaceId;

    private String spaceName;

    private BigDecimal spaceOwnerId;

    private String ownerName;

    private String spaceState;

    private String describe;

    private Date createTime;

    private Date updateTime;

    private Integer spaceMemberNum;

    public BigDecimal getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(BigDecimal spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public BigDecimal getSpaceOwnerId() {
        return spaceOwnerId;
    }

    public void setSpaceOwnerId(BigDecimal spaceOwnerId) {
        this.spaceOwnerId = spaceOwnerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSpaceState() {
        return spaceState;
    }

    public void setSpaceState(String spaceState) {
        this.spaceState = spaceState;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public Integer getSpaceMemberNum() {
        return spaceMemberNum;
    }

    public void setSpaceMemberNum(Integer spaceMemberNum) {
        this.spaceMemberNum = spaceMemberNum;
    }

    @Override
    public String toString() {
        return "WorkSpaceInfoResult{" +
                "spaceId=" + spaceId +
                ", spaceName='" + spaceName + '\'' +
                ", spaceOwnerId=" + spaceOwnerId +
                ", ownerName='" + ownerName + '\'' +
                ", spaceState='" + spaceState + '\'' +
                ", describe='" + describe + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", spaceMemberNum=" + spaceMemberNum +
                '}';
    }
}
