package com.geominfo.mlsql.utils;

import com.geominfo.mlsql.domain.dto.JwtUser;
import com.geominfo.mlsql.domain.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class JwtTokenUtils implements Serializable {

    private static final long serialVersionUID = 5643984248693269567L;

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    //系统中token的标识
    public static final String TOKEN_SERVER = "token-server";

    private static String SECRET = "secret";

    // 过期时间是3600秒，既是1个小时
    private static Long EXPIRATION = 3600L;

    // 选择了记住我之后的过期时间为7天
    private static final Long EXPIRATION_REMEMBER = 604800L;


    // 创建token
    public static String createToken(JwtUser jwtUser, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setId(UUID.randomUUID().toString());
        jwtBuilder.claim("permissions",jwtUser.getPermissions());
        jwtBuilder.setSubject(jwtUser.getUsername());
        jwtBuilder.setIssuer(TOKEN_SERVER);
        jwtBuilder.signWith(SignatureAlgorithm.HS512, SECRET);
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000));
        return jwtBuilder.compact();
    }

    // 从token中获取用户名
    public static JwtUser parseToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        User user = new User();
        user.setUserName(claims.getSubject());
        ArrayList<String> list = (ArrayList<String>) claims.get("permissions");
        String[] perms = new String[list.size()];
        user.setPermissions(list.toArray(perms));
        return new JwtUser(user);
    }

    // 是否已过期
    public static boolean isExpiration(String token){
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

}