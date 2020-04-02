package io.github.talelin.core.annotation;

import io.github.talelin.core.enumeration.UserLevel;

import java.lang.annotation.*;

/**
 * LoginRequired 和 RouteMeta 融合注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Required(level = UserLevel.LOGIN)
public @interface LoginMeta {

    String permission() default "";

    String module() default "";

    boolean mount() default false;
}