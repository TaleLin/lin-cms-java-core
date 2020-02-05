package com.lin.cms.autoconfigure.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;


@SuppressWarnings("ConfigurationProperties")
@ConfigurationProperties("lin.cms")
public class LinCmsProperties {

    private static final String[] DEFAULT_EXCLUDE_METHODS = new String[]{"OPTIONS"};

    private String tokenSecret = "";

    private String[] excludeMethods = DEFAULT_EXCLUDE_METHODS;

    private Long tokenAccessExpire = 3600L;

    private Long tokenRefreshExpire = 2592000L;

    private boolean loggerEnabled = true;

    public boolean isLoggerEnabled() {
        return loggerEnabled;
    }

    public void setLoggerEnabled(boolean loggerEnabled) {
        this.loggerEnabled = loggerEnabled;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public Long getTokenAccessExpire() {
        return tokenAccessExpire;
    }

    public void setTokenAccessExpire(Long tokenAccessExpire) {
        this.tokenAccessExpire = tokenAccessExpire;
    }

    public Long getTokenRefreshExpire() {
        return tokenRefreshExpire;
    }

    public void setTokenRefreshExpire(Long tokenRefreshExpire) {
        this.tokenRefreshExpire = tokenRefreshExpire;
    }

    public String[] getExcludeMethods() {
        return excludeMethods;
    }

    public void setExcludeMethods(String[] excludeMethods) {
        this.excludeMethods = excludeMethods;
    }
}
