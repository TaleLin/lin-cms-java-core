package io.github.talelin.core.annotation;

import io.github.talelin.core.enumeration.UserLevel;

import java.lang.annotation.*;

/**
 * 表示需要权限
 *
 * @author pedro@TaleLin
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Required {
    UserLevel level() default UserLevel.TOURIST;
}
