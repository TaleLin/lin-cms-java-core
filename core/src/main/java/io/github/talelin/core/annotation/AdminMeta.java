package io.github.talelin.core.annotation;

import io.github.talelin.core.enumeration.UserLevel;

import java.lang.annotation.*;

/**
 * AdminRequired 和 RouteMeta 融合注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Required(level = UserLevel.ADMIN)
public @interface AdminMeta {

    String permission() default "";

    String module() default "";

    boolean mount() default false;
}