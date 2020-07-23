package com.geominfo.mlsql.shiro.realm;

import com.geominfo.mlsql.domain.vo.JwtAccount;
import com.geominfo.mlsql.shiro.token.JwtToken;
import com.geominfo.mlsql.util.JsonWebTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * @program: geometry-bi
 * @description: 如果身份验证成功,在进行授权时就通过doGetAuthorizationInfo方法获取角色/权限信息用于授权验证
 * @author: 肖乔辉
 * @create: 2018-08-17 15:39
 * @version: 1.0.0
 */
@Log4j2
public class JwtRealm extends AuthorizingRealm {

    public Class<?> getAuthenticationTokenClass() {
        // 此realm只支持jwtToken
        return JwtToken.class;
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        JwtAccount jwtAccount = (JwtAccount) principalCollection.getPrimaryPrincipal();
        // likely to be json, parse it:
//        if (payload.startsWith("jwt:") && payload.charAt(4) == '{'
//                && payload.charAt(payload.length() - 1) == '}') {
//            Map<String, Object> payloadMap = JsonWebTokenUtil.readValue(payload.substring(4));
            if (jwtAccount!=null){
                Set<String> roles = JsonWebTokenUtil.split(jwtAccount.getRoles());
                Set<String> permissions = JsonWebTokenUtil.split(jwtAccount.getPerms());
                SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();
                if(null!=roles&&!roles.isEmpty())
                    info.setRoles(roles);
                if(null!=permissions&&!permissions.isEmpty())
                    info.setStringPermissions(permissions);
                return info;
            }
//        }
        return null;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof JwtToken)) {
            return null;
        }
        JwtToken jwtToken = (JwtToken)authenticationToken;
         String jwt = (String)jwtToken.getCredentials();
        String payload = null;
        JwtAccount jwtAccount = null;
        try{
            // 预先解析Payload
            // 没有做任何的签名校验
//            payload = JsonWebTokenUtil.parseJwtPayload(jwt);
            jwtAccount = JsonWebTokenUtil.parseJwt(jwt);
        } catch(MalformedJwtException e){
            log.error("令牌格式错误：",e);
            throw new AuthenticationException("errJwt");     //令牌格式错误
        } catch(ExpiredJwtException e){
            log.error("ExpiredJwtException令牌过期：",e);
            throw new AuthenticationException("expiredJwt"); // 令牌过期
        }catch(Exception e){
            log.error("令牌无效：",e);
            throw new AuthenticationException("errsJwt");    //令牌无效
        }
        if(null == jwtAccount){
            throw new AuthenticationException("errJwt");    //令牌无效
        }
        return new SimpleAuthenticationInfo(jwtAccount,jwt,this.getName());
    }
}
