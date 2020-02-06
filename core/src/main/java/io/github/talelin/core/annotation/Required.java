package io.github.talelin.core.annotation;

import io.github.talelin.core.enums.UserLevel;

import java.lang.annotation.*;

/**
 * 表示需要权限
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Required {
    UserLevel level() default UserLevel.TOURIST;
}
