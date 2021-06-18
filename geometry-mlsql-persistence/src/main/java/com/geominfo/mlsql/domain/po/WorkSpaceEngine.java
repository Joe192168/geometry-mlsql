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
 * @create: 2021/6/16 17:50
 * @version: 1.0.0
 */
@TableName("t_workspace_engines")
public class WorkSpaceEngine {

    @TableId(type = IdType.INPUT)
    private BigDecimal id;

    //空间id
    private BigDecimal workspaceId;

    //引擎id
    private BigDecimal engineId;

    //设置默认
    private BigDecimal isDefault;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date operatorTime;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(BigDecimal workspaceId) {
        this.workspaceId = workspaceId;
    }

    public BigDecimal getEngineId() {
        return engineId;
    }

    public void setEngineId(BigDecimal engineId) {
        this.engineId = engineId;
    }

    public BigDecimal getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(BigDecimal isDefault) {
        this.isDefault = isDefault;
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    @Override
    public String toString() {
        return "WorkSpaceEngine{" +
                "id=" + id +
                ", workspaceId=" + workspaceId +
                ", engineId=" + engineId +
                ", isDefault=" + isDefault +
                ", operatorTime=" + operatorTime +
                '}';
    }
}
