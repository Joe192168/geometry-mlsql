package com.geominfo.mlsql.domain.param;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/23 9:55
 * @version: 1.0.0
 */
public class WorkSpaceInfoParam {

    private BigDecimal spaceId;

    private String workSpaceName;

    private BigDecimal spaceOwnerId;

    private String describe;

    public BigDecimal getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(BigDecimal spaceId) {
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
