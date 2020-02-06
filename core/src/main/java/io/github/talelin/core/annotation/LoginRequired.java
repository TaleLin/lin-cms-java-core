package io.github.talelin.core.annotation;


import io.github.talelin.core.enums.UserLevel;

import java.lang.annotation.*;

/**
 * 登录权限
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Required(level = UserLevel.LOGIN)
public @interface LoginRequired {
}
