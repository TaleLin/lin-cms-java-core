package com.lin.cms.core.annotation;

import com.lin.cms.core.enums.UserLevel;

import java.lang.annotation.*;

/**
 * 管理员权限
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Required(level = UserLevel.ADMIN)
public @interface AdminRequired {
}
