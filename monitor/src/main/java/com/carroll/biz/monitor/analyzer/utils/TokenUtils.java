package com.carroll.biz.monitor.analyzer.utils;

//import com.quantumtrip.ida.authentication.entity.TokenMessage;
//import com.quantumtrip.ida.authentication.user.UserTokenUtils;
import com.carroll.biz.monitor.analyzer.auth.TokenMessage;
import com.carroll.biz.monitor.analyzer.auth.UserTokenUtils;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.util.Date;

/**
 * @author: hehongbo
 * @date 2019/9/9
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
public class TokenUtils extends UserTokenUtils {

    public static TokenMessage createToken(String userId, String phone) throws JoseException, IOException {
        JsonWebEncryption jwe = getJWE();
        JwtClaims claims;
        (claims = new JwtClaims()).setClaim("userId", userId);
        claims.setClaim("phone", phone);
        claims.setClaim("tokenCreatedTime", (new Date()).getTime());
        jwe.setPayload(claims.toJson());
        TokenMessage tokenMessage;
        (tokenMessage = new TokenMessage()).setToken(jwe.getCompactSerialization());
        return tokenMessage;
    }
}
