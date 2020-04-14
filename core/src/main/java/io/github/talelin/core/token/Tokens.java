package io.github.talelin.core.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 令牌数据
 *
 * @author pedro@TaleLin
 */
@AllArgsConstructor
public class Tokens {

    @Getter
    private String accessToken;

    @Getter
    private String refreshToken;
}
