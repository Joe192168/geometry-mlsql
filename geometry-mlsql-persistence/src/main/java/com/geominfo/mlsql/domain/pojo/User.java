package com.geominfo.mlsql.domain.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: geometry-bi
 * @description: User
 * @author: LF
 * @create: 2019/11/22
 * @version: 1.0.0
 */
@Data
public class User {

    //用户userId
    private Integer id;
    //用户的资源id
    private Integer resourceId;
    //用户姓名
    private String userName;
    //用户拼音码
    private String phoneticCode;
    //用户登录名
    private String loginName;
    //用户密码
    private String password;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    private String phonesel;
    //最后修改时间
    private Date lastUploadTime;
    //用户邮箱
    private String email;
    //备注
    private String remark;
    //加密盐
    private String salt;
    //权限
    private String[] permissions;
}
