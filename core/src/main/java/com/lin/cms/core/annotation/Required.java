package com.lin.cms.core.annotation;

import com.lin.cms.core.enums.UserLevel;

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
