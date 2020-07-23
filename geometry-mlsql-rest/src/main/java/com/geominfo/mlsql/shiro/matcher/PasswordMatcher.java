package com.geominfo.mlsql.shiro.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;

/**
 * @program: geometry-bi
 * @description: Realm在验证用户身份的时候，要进行密码匹配。最简单的情况就是明文直接匹配，然后就是加密匹配，这里的匹配工作则就是交给CredentialsMatcher来完成的
 * @author: 肖乔辉
 * @create: 2019-05-23 19:02
 * @version: 1.0.0
 */
@Component
public class PasswordMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        return authenticationToken.getPrincipal().toString().equals(authenticationInfo.getPrincipals().getPrimaryPrincipal().toString())
                && authenticationToken.getCredentials().toString().equals(authenticationInfo.getCredentials().toString());
    }
}
