package io.github.talelin.autoconfigure.configuration;

import io.github.talelin.autoconfigure.interceptor.AuthorizeInterceptor;
import io.github.talelin.autoconfigure.interceptor.LogInterceptor;
import io.github.talelin.core.token.DoubleJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * lin cms 配置类
 *
 * @author pedro@TaleLin
 */
@Configuration(proxyBeanMethods = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(LinCmsProperties.class)
public class LinCmsConfiguration {

    @Autowired
    private LinCmsProperties properties;

    /**
     * jwt bean
     *
     * @return jwt bean
     */
    @Bean
    public DoubleJWT jwter() {
        String secret = properties.getTokenSecret();
        Long accessExpire = properties.getTokenAccessExpire();
        Long refreshExpire = properties.getTokenRefreshExpire();
        if (accessExpire == null) {
            // 一个小时
            accessExpire = 60 * 60L;
        }
        if (refreshExpire == null) {
            // 一个月
            refreshExpire = 60 * 60 * 24 * 30L;
        }
        return new DoubleJWT(secret, accessExpire, refreshExpire);
    }

    /**
     * AuthorizeInterceptor bean
     *
     * @return bean
     */
    @Bean
    public AuthorizeInterceptor authInterceptor() {
        String[] excludeMethods = properties.getExcludeMethods();
        return new AuthorizeInterceptor(excludeMethods);
    }

    /**
     * LogInterceptor bean
     *
     * @return bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "lin.cms", value = "logger-enabled", havingValue = "true")
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

}
