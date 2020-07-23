package com.geominfo.mlsql.shiro.matcher;

import com.geominfo.mlsql.domain.vo.JwtAccount;
import com.geominfo.mlsql.util.JsonWebTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;

/**
 * @program: geometry-bi
 * @description: 对jwt进行匹配
 * @author: 肖乔辉
 * @create: 2019-05-23 19:02
 * @version: 1.0.0
 */
@Component
@Log4j2
public class JwtMatcher implements CredentialsMatcher {


    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        String jwt = (String) authenticationInfo.getCredentials();
        JwtAccount jwtAccount = null;
        try{
            jwtAccount = JsonWebTokenUtil.parseJwt(jwt);
        } catch(SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e){
            log.error("SignatureException令牌错误：",e);
            throw new AuthenticationException("errJwt"); // 令牌错误
        } catch(ExpiredJwtException e){
            log.error("ExpiredJwtException令牌过期：",e);
            throw new AuthenticationException("expiredJwt"); // 令牌过期
        } catch(Exception e){
            log.error("Exception令牌错误：",e);
            throw new AuthenticationException("errJwt");// 令牌错误
        }
        if(null == jwtAccount){
            throw new AuthenticationException("errJwt");// 令牌为空
        }

        return true;
    }
}
