package com.lin.cms.core.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 令牌数据
 */
@AllArgsConstructor
public class Tokens {

    @Getter
    private String accessToken;

    @Getter
    private String refreshToken;
}
