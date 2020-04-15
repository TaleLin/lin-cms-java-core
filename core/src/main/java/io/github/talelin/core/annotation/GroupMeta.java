package io.github.talelin.core.annotation;

import io.github.talelin.core.enumeration.UserLevel;

import java.lang.annotation.*;

/**
 * GroupRequired 和 RouteMeta 融合注解
 *
 * @author pedro@TaleLin
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