package com.lin.cms.core.token;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lin.cms.core.utils.DateUtil;

import java.util.Date;
import java.util.Map;


public class SingleJWT {

    private Algorithm algorithm;

    private long expire;


    private JWTVerifier verifier;

    private JWTCreator.Builder builder;

    /**
     * @param algorithm 加密算法
     * @param expire    token过期时间
     */
    public SingleJWT(Algorithm algorithm, long expire) {
        this.algorithm = algorithm;
        this.expire = expire;
        this.initBuilderAndVerifier();
    }

    /**
     * @param secret 不传入加密算法，传入密钥，则默认使用 HMAC256 加密算法
     * @param expire token过期时间
     */
    public SingleJWT(String secret, long expire) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.expire = expire;
        this.initBuilderAndVerifier();
    }

    public String generateToken(String tokenType, long identity, String scope, long expire) {
        Date expireDate = DateUtil.getDurationDate(expire);
        return builder
                .withClaim("type", tokenType)
                .withClaim("identity", identity)
                .withClaim("scope", scope)
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    public Map<String, Claim> decodeToken(String token) {
        DecodedJWT jwt = verifier.verify(token);
        checkTokenExpired(jwt.getExpiresAt());
        return jwt.getClaims();
    }

    private void checkTokenExpired(Date expiresAt) {
        long now = new Date().getTime();
        if (expiresAt.getTime() < now) {
            throw new TokenExpiredException("token is expired");
        }
    }

    /***
     * 获得令牌的验证器
     * @return JWTVerifier
     */
    public JWTVerifier getVerifier() {
        return verifier;
    }

    /**
     * 获得令牌构建器
     *
     * @return Builder
     */
    public JWTCreator.Builder getBuilder() {
        return builder;
    }

    /**
     * 获得加密方法
     *
     * @return Algorithm
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public Long getExpire() {
        return expire;
    }


    private void initBuilderAndVerifier() {
        verifier = com.auth0.jwt.JWT.require(algorithm)
                .acceptExpiresAt(this.expire)
                .build();
        builder = com.auth0.jwt.JWT.create();
    }
}
