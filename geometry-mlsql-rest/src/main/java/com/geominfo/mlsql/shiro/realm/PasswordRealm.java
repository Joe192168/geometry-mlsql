package com.geominfo.mlsql.shiro.realm;

import com.geominfo.mlsql.shiro.provider.AccountProvider;
import com.geominfo.mlsql.shiro.token.PasswordToken;
import com.geominfo.mlsql.utils.MD5Scala;
import com.geominfo.mlsql.utils.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import com.geominfo.mlsql.domain.vo.Account;

/**
 * @program: geometry-bi
 * @description: 这里是登录认证realm
 * @author: 肖乔辉
 * @create: 2018-08-17 15:39
 * @version: 1.0.0
 */
public class PasswordRealm extends AuthorizingRealm {


    private AccountProvider accountProvider;

    //此Realm只支持PasswordToken
    public Class<?> getAuthenticationTokenClass() {
        return PasswordToken.class;
    }


    // 这里只需要认证登录，成功之后派发 json web token 授权在那里进行
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof PasswordToken)) return null;

        if(null==authenticationToken.getPrincipal()||null==authenticationToken.getCredentials()){
            throw new UnknownAccountException();
        }
        String appId = (String)authenticationToken.getPrincipal();
        Account account = accountProvider.loadAccount(appId);
        if (account != null) {
            // 用盐对密码进行MD5加密
            if(StringUtils.isBlank(account.getSalt())){
                ((PasswordToken) authenticationToken).setPassword(MD5Scala.md5(((PasswordToken) authenticationToken).getPassword()).toLowerCase());
            }else{
                ((PasswordToken) authenticationToken).setPassword(MD5Scala.md5(((PasswordToken) authenticationToken).getPassword()+account.getSalt()).toLowerCase());
            }
            return new SimpleAuthenticationInfo(appId,account.getPassword().toLowerCase(),getName());
        } else {
            return new SimpleAuthenticationInfo(appId,"",getName());
        }

    }

    public void setAccountProvider(AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
    }
}
