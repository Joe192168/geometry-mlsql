package com.geominfo.mlsql.domain.param;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/23 10:26
 * @version: 1.0.0
 */
public class WorkSpaceMemberParam {

    private BigDecimal spaceId;

    private BigDecimal spaceOwnerId;

    private BigDecimal spaceMemberId;

    private String spaceState;

    public BigDecimal getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(BigDecimal spaceId) {
        this.spaceId = spaceId;
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

    public String getSpaceState() {
        return spaceState;
    }

    public void setSpaceState(String spaceState) {
        this.spaceState = spaceState;
    }
}
