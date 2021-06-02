package com.geominfo.mlsql.domain.vo;

import com.geominfo.authing.common.enums.EnumApplicationResource;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2020/11/24 10:37
 * @version: 1.0.0
 */
public class OperateLogVo {

    /**
     * 操作人id
     */
    private Integer operateUserId;

    /**
     * 操作应用id
     */
    private Integer operateAppId;

    /**
     * 操作日志类型（0-登录，1-登出）
     */
    private Integer operateType;

    /**
     * ip 地址
     */
    private String operateIp;

    /**
     * 操作日志
     */
    private String operateLog;

    /**
     * 应用系统id
     */
    private BigDecimal operateResourceId = new BigDecimal(EnumApplicationResource.MLSQL.getResourceId());

    public Integer getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(Integer operateUserId) {
        this.operateUserId = operateUserId;
    }

    public Integer getOperateAppId() {
        return operateAppId;
    }

    public void setOperateAppId(Integer operateAppId) {
        this.operateAppId = operateAppId;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

    public String getOperateLog() {
        return operateLog;
    }

    public void setOperateLog(String operateLog) {
        this.operateLog = operateLog;
    }

    public BigDecimal getOperateResourceId() {
        return operateResourceId = new BigDecimal(EnumApplicationResource.MLSQL.getResourceId());
    }

    public void setOperateResourceId(BigDecimal operateResourceId) {
        this.operateResourceId = operateResourceId;
    }

    @Override
    public String toString() {
        return "OperateLogVo{" +
                "operateUserId=" + operateUserId +
                ", operateAppId=" + operateAppId +
                ", operateType=" + operateType +
                ", operateIp='" + operateIp + '\'' +
                ", operateLog='" + operateLog + '\'' +
                ", operateResourceId=" + operateResourceId +
                '}';
    }
}
