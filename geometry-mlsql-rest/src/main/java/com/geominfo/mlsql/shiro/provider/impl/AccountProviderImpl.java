package com.geominfo.mlsql.shiro.provider.impl;


import com.geominfo.mlsql.domain.vo.Account;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.shiro.provider.AccountProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: geometry-bi
 * @description: 查询账户接口
 * @author: 肖乔辉
 * @create: 2019-04-19 18:48
 * @version: 1.0.0
 */
@Service("AccountProvider")
public class AccountProviderImpl implements AccountProvider {

      @Autowired
      private UserService accountService;

    public Account loadAccount(String appId) {
        return accountService.loadAccount(appId);
    }
}
