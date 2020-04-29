package io.github.talelin.core.token;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import io.github.talelin.core.util.DateUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Juzi@TaleLin
 */
public class SingleJWTTest {

    private static final Logger log = LoggerFactory.getLogger(SingleJWTTest.class);

    @Test
    public void generateToken() {
        SingleJWT jwt = new SingleJWT("secret", 1000);
        String token = jwt.generateToken("test", 1, "test", 1000);
        assertNotNull(token);
        log.info(token);
    }

    @Test
    public void getSpecifyToken() {
        SingleJWT jwt = new SingleJWT("secret", 1000);
        String token = jwt.getBuilder()
                .withClaim("type", "test")
                .withClaim("identity", 1)
                .withClaim("scope", "test")
                .withExpiresAt(DateUtil.getDurationDate(10000))
                .sign(jwt.getAlgorithm());
        assertNotNull(token);
        log.info(token);
    }

    @Test
    public void decodeToken() {
        SingleJWT jwt = new SingleJWT("secret", 10000);
        String token = jwt.getBuilder()
                .withClaim("type", "test")
                .withClaim("identity", 1)
                .withClaim("scope", "test")
                .withExpiresAt(DateUtil.getDurationDate(10000))
                .sign(jwt.getAlgorithm());
        assertNotNull(token);
        log.info(token);
        Map<String, Claim> claimMap = jwt.decodeToken(token);
        log.info("{}", claimMap);
        assertEquals(claimMap.get("type").asString(), "test");
        assertEquals(claimMap.get("scope").asString(), "test");
    }

    @Test
    public void getVerifier() {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        SingleJWT jwt = new SingleJWT(algorithm, 1000);
        assertNotNull(jwt.getVerifier());
    }

    @Test
    public void getBuilder() {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        SingleJWT jwt = new SingleJWT(algorithm, 1000);
        assertNotNull(jwt.getBuilder());
    }

    @Test
    public void getAlgorithm() {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        SingleJWT jwt = new SingleJWT(algorithm, 1000);
        assertNotNull(jwt.getAlgorithm());
    }

    @Test
    public void getExpire() {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        SingleJWT jwt = new SingleJWT(algorithm, 1000);
        assertEquals(1000L, (long) jwt.getExpire());
    }
}