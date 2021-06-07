package com.geominfo.mlsql.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @title: TSystemResourcesDO
 * @date 2021/5/1214:33
 */
@Data
@TableName("t_system_resources")
public class TSystemResources {

    @ApiModelProperty(value = "ID", name="id", required = true)
    @TableId(type = IdType.INPUT)
    private BigDecimal id;
    @ApiModelProperty(value = "父资源ID", name="parentId")
    private BigDecimal parentid;
    @ApiModelProperty(value = "资源类型ID", name="resourceTypeId")
    private BigDecimal resourceTypeId;
    @ApiModelProperty(value = "资源名称", name="resourceName")
    private String resourceName;
    @ApiModelProperty(value = "显示名称", name="displayName")
    private String displayName;
    @ApiModelProperty(value = "显示顺序", name="displayOrder")
    private BigDecimal displayOrder;
    @ApiModelProperty(value = "说明", name="description")
    private String description;
    @ApiModelProperty(value = "显示图片", name="displayImg")
    private String displayImg;
    @ApiModelProperty(value = "资源级别", name="resourceLevel")
    private String resourceLevel;
    @ApiModelProperty(value = "是否内部资源", name="isInnerResource")
    private BigDecimal isInnerResource;
    @ApiModelProperty(value = "所有者", name="owner")
    private BigDecimal owner;
    @ApiModelProperty(value = "创建时间", name="createTime")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "更新时间", name="updateTime")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
    @ApiModelProperty(value = "最后修改人", name="lastEditor")
    private BigDecimal lastEditor;
    @ApiModelProperty(value = "资源状态", name="resourceState")
    private BigDecimal resourceState;
    @ApiModelProperty(value = "外部程序信息", name="externalAppInfo")
    private String externalAppInfo;
    @ApiModelProperty(value = "派生资源ID", name="deriveResourceId")
    private BigDecimal deriveResourceId;
    @ApiModelProperty(value = "内容信息", name="contentInfo")
    private String contentInfo;
    @ApiModelProperty(value = "访问方式 GET POST PUT DELETE PATCH", name="method")
    @TableField(exist = false)
    private String method;
}
