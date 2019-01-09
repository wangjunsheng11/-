package com.kakacl.product_service.utils;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description JWT安全类
 * @date 2019-01-09
 */
public class JWTUtils {

    static String  key="fyYEeStTvniV-8Utwk8-TSMpxsPgSlpIhawBunr3MUk";

    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {

        //我们将用我们的ApiKey secret来签名我们的JWT
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //我们将用我们的ApiKey secret来签名我们的JWT
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //让我们设置JWT索赔
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        //已经被指定了，让我们把过期的时间加到
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //构建JWT并将其序列化为一个紧凑的、url安全的字符串
        return builder.compact();
    }
    public static Claims parseJWT(String jwt) throws Exception {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(key))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

}
