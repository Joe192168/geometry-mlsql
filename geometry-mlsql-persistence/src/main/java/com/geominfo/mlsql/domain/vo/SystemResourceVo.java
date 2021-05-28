package com.geominfo.mlsql.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

public class SystemResourceVo {

    private BigDecimal id;
    private BigDecimal parentId;
    private BigDecimal resourceTypeId;
    private String resourceTypeName;
    private String resourceName;
    private String displayName;
    private BigDecimal displayOrder;
    private String description;
    private String displayImg;
    private String resourceLevel;
    private BigDecimal owner;
    private Date createTime;
    private Date updateTime;
    private BigDecimal deriveResourceId;
    private String contentInfo;
    private String method;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getParentId() {
        return parentId;
    }

    public void setParentId(BigDecimal parentId) {
        this.parentId = parentId;
    }

    public BigDecimal getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(BigDecimal resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public BigDecimal getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(BigDecimal displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayImg() {
        return displayImg;
    }

    public void setDisplayImg(String displayImg) {
        this.displayImg = displayImg;
    }

    public String getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(String resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    public BigDecimal getOwner() {
        return owner;
    }

    public void setOwner(BigDecimal owner) {
        this.owner = owner;
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

    public BigDecimal getDeriveResourceId() {
        return deriveResourceId;
    }

    public void setDeriveResourceId(BigDecimal deriveResourceId) {
        this.deriveResourceId = deriveResourceId;
    }

    public String getContentInfo() {
        return contentInfo;
    }

    public void setContentInfo(String contentInfo) {
        this.contentInfo = contentInfo;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
