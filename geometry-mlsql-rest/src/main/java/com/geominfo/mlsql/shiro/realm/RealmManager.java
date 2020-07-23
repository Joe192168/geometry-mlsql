package com.geominfo.mlsql.shiro.realm;


import com.geominfo.mlsql.shiro.matcher.JwtMatcher;
import com.geominfo.mlsql.shiro.matcher.PasswordMatcher;
import com.geominfo.mlsql.shiro.provider.AccountProvider;
import com.geominfo.mlsql.shiro.token.JwtToken;
import com.geominfo.mlsql.shiro.token.PasswordToken;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: geometry-bi
 * @description: realm管理器
 * @author: 肖乔辉
 * @create: 2018-08-17 15:39
 * @version: 1.0.0
 */
@Component
public class RealmManager {

    private PasswordMatcher passwordMatcher;
    private JwtMatcher jwtMatcher;
    private AccountProvider accountProvider;
    @Autowired
    public RealmManager(AccountProvider accountProvider,PasswordMatcher passwordMatcher,JwtMatcher jwtMatcher) {
        this.accountProvider = accountProvider;
        this.passwordMatcher = passwordMatcher;
        this.jwtMatcher = jwtMatcher;
    }

    public List<Realm> initGetRealm() {
        List<Realm> realmList = new LinkedList<>();
        // ----- password
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setAccountProvider(accountProvider);
        passwordRealm.setCredentialsMatcher(passwordMatcher);
        passwordRealm.setAuthenticationTokenClass(PasswordToken.class);
        realmList.add(passwordRealm);
        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCredentialsMatcher(jwtMatcher);
        jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);
        return Collections.unmodifiableList(realmList);
    }
}
