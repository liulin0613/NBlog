package com.nblog.utils;

import com.nblog.dto.Audience;
import com.nblog.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Token 工具类
 *
 * JWT是由三段信息构成：header playload signature
 *
 *      jwt的头部承载两部分信息：
 *          声明类型，这里是jwt
 *          声明加密的算法 通常直接使用 HMAC SHA256
 *
 *      playload 就是存放有效信息的地方
 *
 *      jwt的第三部分是一个签证信息，这个签证信息由三部分组成：
 *          header (base64后的)
 *          payload (base6的后的)
 *          secret
 *      这个部分需要base64加密后的header和base64加密后的payload使用.
 *      连接组成的字符串，然后通过header中声明的加密方式进行加盐secret组合加密，
 *      然后就构成了jwt的第三部分
 *
 */
@Slf4j
public final class JwtTokenUtil {
    /**
     * 解析 jwt
     * @param jsonWebToken token
     * @param base64Security  私钥
     * @return
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception e){
            throw new BizException("无效 token，请重新登录");
        }
    }

    /**
     * 构建 jwt
     * @param userId 用户 id
     * @param username 用户名
     * @param audience  audience
     * @return token
     */
    public static String createJWT(String userId, String username,Audience audience) {
        try {
            // 使用HS256加密算法
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            //生成签名密钥
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(audience.getBase64Secret());
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
            //userId是重要信息，进行加密下
            String encryId = new String(Base64.getMimeEncoder().encode(userId.getBytes()));
            //添加构成JWT的参数
            JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                    // 可以将基本不重要的对象信息放到claims
                    .claim("userId", encryId)
                    .setSubject(username)           // 代表这个JWT的主体，即它的所有人
                    .setIssuer(audience.getClientId())              // 代表这个JWT的签发主体；
                    .setIssuedAt(new Date())        // 是一个时间戳，代表这个JWT的签发时间；
                    .setAudience(audience.getName())          // 代表这个JWT的接收对象；
                    .signWith(signingKey);

            //添加Token过期时间
            int TTLMillis = audience.getExpiresSecond();
            if (TTLMillis >= 0) {
                long expMillis = nowMillis + TTLMillis;
                Date exp = new Date(expMillis);
                builder.setExpiration(exp)  // 是一个时间戳，代表这个JWT的过期时间；
                        .setNotBefore(now); // 是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
            }

            //生成JWT
            return builder.compact();
        } catch (Exception e) {
            log.error("签名失败", e);
            return "";
        }
    }

    /**
     * 从 token 中获取用户名
     * @param token token
     * @param base64Security 私钥
     * @return
     */
    public static String getUserName(String token, String base64Security){
        return parseJWT(token, base64Security).getSubject();
    }

    /**
     * 从token中获取用户ID
     * @param token token
     * @param base64Security 私钥
     * @return
     */
    public static String getUserId(String token, String base64Security){
        String userId = parseJWT(token, base64Security).get("userId", String.class);
        return new String(Base64.getMimeDecoder().decode(userId));
    }

    /**
     * 是否已过期
     * @param token token
     * @param base64Security 私钥
     * @return
     */
    public static boolean isExpiration(String token, String base64Security) {
        return parseJWT(token, base64Security).getExpiration().before(new Date());
    }
}
