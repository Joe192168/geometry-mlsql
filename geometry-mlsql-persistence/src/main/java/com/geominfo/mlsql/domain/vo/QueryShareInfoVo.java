package com.geominfo.mlsql.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/7/2 17:06
 * @version: 1.0.0
 */
public class QueryShareInfoVo {
    //分享人id
    private BigDecimal shareUserId;
    //分享开始时间
    private Date shareStartTime;
    //分享结束时间
    private Date shareEndTime;
    //被分享人id
    private BigDecimal sharedUserId;

    public BigDecimal getShareUserId() {
        return shareUserId;
    }

    public void setShareUserId(BigDecimal shareUserId) {
        this.shareUserId = shareUserId;
    }

    public BigDecimal getSharedUserId() {
        return sharedUserId;
    }

    public void setSharedUserId(BigDecimal sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    public Date getShareStartTime() {
        return shareStartTime;
    }

    public void setShareStartTime(Date shareStartTime) {
        this.shareStartTime = shareStartTime;
    }

    public Date getShareEndTime() {
        return shareEndTime;
    }

    public void setShareEndTime(Date shareEndTime) {
        this.shareEndTime = shareEndTime;
    }

    @Override
    public String toString() {
        return "QueryShareInfoVo{" +
                "sharedUserId=" + sharedUserId +
                ", shareStartTime=" + shareStartTime +
                ", shareEndTime=" + shareEndTime +
                '}';
    }
}
