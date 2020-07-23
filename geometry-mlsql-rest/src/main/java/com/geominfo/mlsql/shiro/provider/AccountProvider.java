package com.geominfo.mlsql.shiro.provider;


import com.geominfo.mlsql.domain.vo.Account;

/**
 * @program: geometry-bi
 * @description: 数据库用户密码账户提供
 * @author: 肖乔辉
 * @create: 2019-04-19 18:48
 * @version: 1.0.0
 */
public interface AccountProvider {

    Account loadAccount(String appId);

}
