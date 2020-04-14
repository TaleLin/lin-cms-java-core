package io.github.talelin.core.annotation;

import java.lang.annotation.*;

/**
 * 路由信息
 *
 * @author pedro@TaleLin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RouteMeta {

    String permission() default "";

    String module() default "";

    boolean mount() default false;
}
