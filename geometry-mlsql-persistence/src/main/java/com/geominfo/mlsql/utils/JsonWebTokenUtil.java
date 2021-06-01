package com.geominfo.mlsql.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geominfo.mlsql.domain.vo.JwtAccount;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: geometry-bi
 * @description: JsonWebTokenUtil工具类
 * @author: 肖乔辉
 * @create: 2019-04-19 18:48
 * @version: 1.0.0
 */
public class JsonWebTokenUtil {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    public static final String SECRET_KEY = "jhsz";
    public static final long refreshPeriodTime = 36000L;//失效时间，单位毫秒
    private static final SecretKey JWT_KEY;// 使用JWT密匙生成的加密key
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        byte[] encodedKey = Base64.decodeBase64(SECRET_KEY);
        JWT_KEY = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    private JsonWebTokenUtil() {

    }

    /**
     * json web token 签发
     * @param id 令牌ID
     * @param subject 用户主体
     * @param issuer 签发人
     * @param validityTime 有效时间(毫秒)
     * @param roles 访问主张-角色
     * @param permissions 访问主题-权限
     * @return
     */
    public static String issueJWT(String id,String subject, String issuer, Long validityTime, String roles, String permissions) {
        // 当前时间戳
        Long currentTimeMillis = System.currentTimeMillis();
        JwtBuilder jwtBuilder = Jwts.builder();
        if (StringUtils.isNotEmpty(id)) {
            jwtBuilder.setId(id.replace("-", "").substring(0, 8));
        }
        if (StringUtils.isNotEmpty(subject)) {
            jwtBuilder.setSubject(subject);
        }
        if (StringUtils.isNotEmpty(issuer)) {
            jwtBuilder.setIssuer(issuer);
        }
        // 设置签发时间
        jwtBuilder.setIssuedAt(new Date(currentTimeMillis));
        // 设置到期时间
        if (null != validityTime) {
            jwtBuilder.claim("validityTime",validityTime);
            jwtBuilder.setExpiration(new Date(currentTimeMillis+validityTime*1000));
        }
        if (StringUtils.isNotEmpty(roles)) {
            jwtBuilder.claim("roles",roles);
        }
        if (StringUtils.isNotEmpty(permissions)) {
            jwtBuilder.claim("perms",permissions);
        }
        // 压缩，可选GZIP
        //jwtBuilder.compressWith(CompressionCodecs.DEFLATE);
        // 加密设置
        jwtBuilder.signWith(SIGNATURE_ALGORITHM,JWT_KEY);
        return jwtBuilder.compact();
    }

    /**
     * 解析JWT
     *
     * @param jwt json web token
     */
    public static JwtAccount parseJwt(String jwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(jwt).getBody();
        JwtAccount jwtAccount = new JwtAccount();
        jwtAccount.setTokenId(claims.getId());// 令牌ID
        jwtAccount.setAppId(claims.getSubject());// 客户标识
        jwtAccount.setIssuer(claims.getIssuer());// 签发者
        jwtAccount.setIssuedAt(claims.getIssuedAt());// 签发时间
        jwtAccount.setAudience(claims.getAudience());// 接收方
        jwtAccount.setRoles(claims.get("roles", String.class));// 访问主张-角色
        jwtAccount.setPerms(claims.get("perms", String.class));// 访问主张-权限
        jwtAccount.setValidityTime(claims.get("validityTime", Long.class));//有效时间
        return jwtAccount;
    }

    /**
     * 分割字符串进SET
     */
    @SuppressWarnings("unchecked")
    public static Set<String> split(String str) {
        Set<String> set = new HashSet<>();
        if (StringUtils.isEmpty(str))
            return set;
        set.addAll(CollectionUtils.arrayToList(str.split(",")));
        return set;
    }

}
