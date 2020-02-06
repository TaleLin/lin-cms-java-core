package com.lin.cms.core.annotation;

import java.lang.annotation.*;

/**
 * 路由信息
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RouteMeta {

    String permission() default "";

    String module() default "";

    boolean mount() default false;
}
