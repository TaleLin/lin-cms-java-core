package com.lin.cms.core.annotation;

import java.lang.annotation.*;

/**
 * 行为日志记录
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logger {
    String template();
}
