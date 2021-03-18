package com.geominfo.mlsql.domain.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @program: geometry-bi
 * @description:
 * @author: 肖乔辉
 * @create: 2018-11-17 11:31
 * @version: 3.0.0
 */
@Data
public class RolePermRule implements Serializable {

    private static final long serialVersionUID = 1L;

    private String url; // 资源URL
    private String needRoles; // 访问资源所需要的角色列表，多个列表用逗号间隔
}
