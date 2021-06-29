package com.geominfo.mlsql.domain.result;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/23 14:47
 * @version: 1.0.0
 */
public class SpaceMemberResult {
    private String userName;
    private String loginName;
    private Date createTime;
    private BigDecimal spaceId;
    private BigDecimal memberId;
    private BigDecimal ownerId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(BigDecimal spaceId) {
        this.spaceId = spaceId;
    }

    public BigDecimal getMemberId() {
        return memberId;
    }

    public void setMemberId(BigDecimal memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(BigDecimal ownerId) {
        this.ownerId = ownerId;
    }
}