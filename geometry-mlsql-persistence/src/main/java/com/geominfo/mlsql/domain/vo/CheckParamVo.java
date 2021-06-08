package com.geominfo.mlsql.domain.vo;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description: 校验参数
 * @author: LF
 * @create: 2021/6/8 11:16
 * @version: 1.0.0
 */
public class CheckParamVo {

    private BigDecimal id;

    private  String paramName;

    private Integer resourceType;

    private Integer parentId;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "CheckParamVo{" +
                "id=" + id +
                ", paramName='" + paramName + '\'' +
                ", resourceType=" + resourceType +
                ", parentId=" + parentId +
                '}';
    }
}
