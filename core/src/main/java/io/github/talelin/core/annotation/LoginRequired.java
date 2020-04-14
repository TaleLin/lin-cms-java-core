package io.github.talelin.core.annotation;


import io.github.talelin.core.enumeration.UserLevel;

import java.lang.annotation.*;

/**
 * 登录权限
 *
 * @author pedro@TaleLin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Required(level = UserLevel.LOGIN)
public @interface LoginRequired {
}
