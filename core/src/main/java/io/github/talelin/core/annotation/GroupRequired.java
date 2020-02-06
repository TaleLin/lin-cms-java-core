package io.github.talelin.core.annotation;

import io.github.talelin.core.enums.UserLevel;

import java.lang.annotation.*;

/**
 * 分组权限
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Required(level = UserLevel.GROUP)
public @interface GroupRequired {
}
