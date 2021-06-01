package com.geominfo.mlsql.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: geometry-bi
 * @description: jwt实体
 * @author: 肖乔辉
 * @create: 2018-08-17 15:39
 * @version: 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAccount implements Serializable {

    private static final long serialVersionUID = -895875540581785581L;

    private String tokenId;// 令牌id
    private String appId;// 客户标识（用户名、账号）
    private String issuer;// 签发者(JWT令牌此项有值)
    private Date issuedAt;// 签发时间
    private String audience;// 接收方(JWT令牌此项有值)
    private String roles;// 访问主张-角色(JWT令牌此项有值)
    private String perms;// 访问主张-资源(JWT令牌此项有值)
    private String host;// 客户地址
    private String tokeKey;//token唯一标识
    private Long validityTime;//有效期

}
