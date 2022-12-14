package com.carroll.biz.monitor.analyzer.auth;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

/**
 * token工具类
 *
 * @author zhangjie
 * @create 2017-02-24 11:00
 **/
public class UserTokenUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTokenUtils.class);
    private static final byte[] key = {-6, 121, 109, -102, 111, 106, -66, -81, -10, -18, -11, -37, 23, -70, -49, -69};

    public static final ThreadLocal<UserTokenEntity> tokenHolder = new ThreadLocal<>();

    /**
     * 获得KEY
     *
     * @return key
     */
    public static Key getKey() {
        return new AesKey(key);
    }

    /**
     * 获得密匙
     *
     * @return jwe
     */
    public static JsonWebEncryption getJWE() {
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(getKey());
        return jwe;
    }

    /**
     * 创建token 并保存到redis
     *
     * @param userId userId
     * @param phone  电话号码
     * @return TokenMessage
     * @throws JoseException
     */
    public static TokenMessage createToken(Long userId, String phone) throws JoseException, IOException {
        JsonWebEncryption jwe = getJWE();
        JwtClaims claims = new JwtClaims();
        claims.setClaim("userId", userId);
        claims.setClaim("phone", phone);
        claims.setClaim("tokenCreatedTime", new Date().getTime());
        jwe.setPayload(claims.toJson());
        TokenMessage tokenMessage = new TokenMessage();
        tokenMessage.setToken(jwe.getCompactSerialization());
        return tokenMessage;
    }

    /**
     * 校验token
     *
     * @param token
     * @param tokenMessage
     * @return
     * @throws Exception
     */
    public static boolean verifyToken(String token, TokenMessage tokenMessage) throws Exception {
        //被校验的token解析
        LOGGER.info("Token:" + token + ",tokenMessage:" + tokenMessage);
        UserTokenEntity tokenModel = parseToken(token);
        if (tokenModel != null) {
            String tokenPhone = tokenModel.getPhone();
            String tokenUserId = tokenModel.getUserId();
            long tokenTime = tokenModel.getTokenCreatedTime();

            if (tokenMessage == null) return false;
            UserTokenEntity userTokenEntity = parseToken(tokenMessage.getToken());
            String redisUserId = userTokenEntity.getUserId();
            String redisPhone = userTokenEntity.getPhone();
            long redisTime = userTokenEntity.getTokenCreatedTime();

            LOGGER.info("redisUserId:" + redisUserId);
            LOGGER.info("tokenUserId:" + tokenUserId);
            LOGGER.info("redisPhone:" + redisPhone);
            LOGGER.info("tokenPhone:" + tokenPhone);
            LOGGER.info("redisTime:" + redisTime);
            LOGGER.info("tokenTime:" + tokenTime);

            LOGGER.info((redisUserId.equals(tokenUserId)
                    && redisPhone.equals(tokenPhone) && redisTime == tokenTime) + "");
            //比较token中的值
            if (redisUserId.equals(tokenUserId)
                    && redisPhone.equals(tokenPhone) && redisTime == tokenTime) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解析Token
     *
     * @param token token
     * @return token
     */
    public static UserTokenEntity parseToken(String token) throws JoseException, InvalidJwtException {
        UserTokenEntity entity = new UserTokenEntity();
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setKey(getKey());
        jwe.setCompactSerialization(token);
        JwtClaims claims = JwtClaims.parse(jwe.getPayload());
        entity.setUserId((String) claims.getClaimValue("userId"));
        entity.setPhone((String)claims.getClaimValue("phone"));
        entity.setTokenCreatedTime(Long.parseLong(claims.getClaimValue("tokenCreatedTime").toString()));
        tokenHolder.set(entity);
        return entity;
    }

    public static UserTokenEntity getTokenEntity() {
        return tokenHolder.get();
    }

//    public static void main(String[] args) throws Exception {
////        TokenMessage tokenMessage = createToken(3L,"13678072713");
//        System.out.println(Arrays.toString(ByteUtil.randomBytes(16)));
////        UserTokenEntity entity =  parseToken(tokenMessage);
////        System.out.println("userId: " + entity.getUserId());
////        System.out.println("phone: " + entity.getPhone());
////        System.out.println("time: " + entity.getTokenCreatedTime());
//    }
}
