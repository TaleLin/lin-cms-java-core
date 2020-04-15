package io.github.talelin.core.annotation;

import java.lang.annotation.*;

/**
 * 行为日志记录
 *
 * @author pedro@TaleLin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logger {
    String template();
}
