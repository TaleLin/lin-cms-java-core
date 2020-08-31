package io.github.talelin.core.token;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.talelin.core.util.DateUtil;
import io.github.talelin.core.constant.TokenConstant;

import java.util.Date;
import java.util.Map;

/**
 * 支持双令牌模式
 * access、refresh token
 *
 * @author pedro@TaleLin
 * @author colorful@TaleLin
 */
public class DoubleJWT {

    private long accessExpire;

    private long refreshExpire;

    private Algorithm algorithm;

    private JWTVerifier accessVerifier;

    private JWTVerifier refreshVerifier;

    private JWTCreator.Builder builder;

    /**
     * 传入加密算法，双token模式
     *
     * @param algorithm     加密算法
     * @param accessExpire  access_token过期时间
     * @param refreshExpire refresh_token过期时间
     */
    public DoubleJWT(Algorithm algorithm, long accessExpire, long refreshExpire) {
        this.algorithm = algorithm;
        this.accessExpire = accessExpire;
        this.refreshExpire = refreshExpire;
        this.initBuilderAndVerifier();
    }

    /**
     * 不传入加密算法，传入密钥，则默认使用 HMAC256 加密算法
     * 双token模式
     *
     * @param secret        加密算法
     * @param accessExpire  access_token过期时间
     * @param refreshExpire refresh_token过期时间
     */
    public DoubleJWT(String secret, long accessExpire, long refreshExpire) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.accessExpire = accessExpire;
        this.refreshExpire = refreshExpire;
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

    public String generateToken(String tokenType, String identity, String scope, long expire) {
        Date expireDate = DateUtil.getDurationDate(expire);
        return builder
                .withClaim("type", tokenType)
                .withClaim("identity", identity)
                .withClaim("scope", scope)
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    public Map<String, Claim> decodeAccessToken(String token) {
        DecodedJWT jwt = accessVerifier.verify(token);
        checkTokenExpired(jwt.getExpiresAt());
        checkTokenType(jwt.getClaim("type").asString(), TokenConstant.ACCESS_TYPE);
        checkTokenScope(jwt.getClaim("scope").asString());
        return jwt.getClaims();
    }

    public Map<String, Claim> decodeRefreshToken(String token) {
        DecodedJWT jwt = refreshVerifier.verify(token);
        checkTokenExpired(jwt.getExpiresAt());
        checkTokenType(jwt.getClaim("type").asString(), TokenConstant.REFRESH_TYPE);
        checkTokenScope(jwt.getClaim("scope").asString());
        return jwt.getClaims();
    }

    private void checkTokenScope(String scope) {
        if (scope == null || !scope.equals(TokenConstant.LIN_SCOPE)) {
            throw new InvalidClaimException("token scope is invalid");
        }
    }

    private void checkTokenType(String type, String accessType) {
        if (type == null || !type.equals(accessType)) {
            throw new InvalidClaimException("token type is invalid");
        }
    }

    private void checkTokenExpired(Date expiresAt) {
        long now = System.currentTimeMillis();
        if (expiresAt.getTime() < now) {
            throw new TokenExpiredException("token is expired");
        }
    }

    public String generateAccessToken(long identity) {
        return generateToken(TokenConstant.ACCESS_TYPE, identity, TokenConstant.LIN_SCOPE, this.accessExpire);
    }

    public String generateAccessToken(String identity) {
        return generateToken(TokenConstant.ACCESS_TYPE, identity, TokenConstant.LIN_SCOPE, this.accessExpire);
    }

    public String generateRefreshToken(long identity) {
        return generateToken(TokenConstant.REFRESH_TYPE, identity, TokenConstant.LIN_SCOPE, this.refreshExpire);
    }

    public String generateRefreshToken(String identity) {
        return generateToken(TokenConstant.REFRESH_TYPE, identity, TokenConstant.LIN_SCOPE, this.refreshExpire);
    }

    public Tokens generateTokens(long identity) {
        String access = this.generateToken(TokenConstant.ACCESS_TYPE, identity, TokenConstant.LIN_SCOPE, this.accessExpire);
        String refresh = this.generateToken(TokenConstant.REFRESH_TYPE, identity, TokenConstant.LIN_SCOPE, this.refreshExpire);
        return new Tokens(access, refresh);
    }

    public Tokens generateTokens(String identity) {
        String access = this.generateToken(TokenConstant.ACCESS_TYPE, identity, TokenConstant.LIN_SCOPE, this.accessExpire);
        String refresh = this.generateToken(TokenConstant.REFRESH_TYPE, identity, TokenConstant.LIN_SCOPE, this.refreshExpire);
        return new Tokens(access, refresh);
    }


    /***
     * 获得令牌的验证器
     * @return JWTVerifier
     */
    public JWTVerifier getAccessVerifier() {
        return accessVerifier;
    }

    /***
     * 获得令牌的验证器
     * @return JWTVerifier
     */
    public JWTVerifier getRefreshVerifier() {
        return refreshVerifier;
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

    public Long getAccessExpire() {
        return accessExpire;
    }


    public Long getRefreshExpire() {
        return refreshExpire;
    }

    private void initBuilderAndVerifier() {
        accessVerifier = com.auth0.jwt.JWT.require(algorithm)
                .acceptExpiresAt(this.accessExpire)
                .build();
        refreshVerifier = com.auth0.jwt.JWT.require(algorithm)
                .acceptExpiresAt(this.refreshExpire)
                .build();
        builder = com.auth0.jwt.JWT.create();
    }

}
