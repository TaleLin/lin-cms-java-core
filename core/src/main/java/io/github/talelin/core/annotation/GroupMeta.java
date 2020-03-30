package io.github.talelin.core.annotation;

import io.github.talelin.core.enums.UserLevel;

import java.lang.annotation.*;

/**
 * GroupRequired 和 RouteMeta 融合注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Required(level = UserLevel.GROUP)
public @interface GroupMeta {

    String permission() default "";

    String module() default "";

    boolean mount() default false;
}