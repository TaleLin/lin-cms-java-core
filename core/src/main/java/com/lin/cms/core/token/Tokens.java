package com.lin.cms.core.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Tokens {

    @Getter
    private String accessToken;

    @Getter
    private String refreshToken;
}
