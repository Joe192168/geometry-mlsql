package com.geominfo.mlsql.domain.param;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/23 16:30
 * @version: 1.0.0
 */
public class AccountParam {
    private BigDecimal id;
    private String loginName;
    private String password;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AccountParam{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
