package com.geominfo.mlsql.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: geometry-bi
 * @description: 工作空间信息表
 * @author: LF
 * @create: 2021/6/9 9:24
 * @version: 1.0.0
 */
@TableName("t_workspace_member_infos")
public class WorkSpaceMember {

    @ApiModelProperty(value = "ID", name="id", required = true)
    @TableId(type = IdType.INPUT)
    private BigDecimal id;

    @ApiModelProperty(value = "工作空间id", name="workSpaceId")
    private BigDecimal spaceId;

    @ApiModelProperty(value = "空间所属者ID", name="spaceOwnerId")
    private BigDecimal spaceOwnerId;

    @ApiModelProperty(value = "空间成员ID", name="spaceMemberId")
    private BigDecimal spaceMemberId;

    @ApiModelProperty(value = "空间标识", name="state")
    private String state;

    @ApiModelProperty(value = "创建时间", name="createTime")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
