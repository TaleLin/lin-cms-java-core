package io.github.talelin.core.annotation;

import io.github.talelin.core.enumeration.UserLevel;

import java.lang.annotation.*;

/**
 * LoginRequired 和 PermissionMeta 融合注解
 *
 * @author pedro@TaleLin
 * @author colorful@TaleLin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Required(level = UserLevel.LOGIN)
@Deprecated
public @interface LoginMeta {

    String value() default "";

    String permission() default "";

    String module() default "";

    boolean mount() default true;

}