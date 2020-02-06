package io.github.talelin.autoconfigure.validator;

import javax.validation.Payload;

/**
 * 自定义的字符串长度校验器
 * 可允许字符串为Blank的时候不校验
 */
public @interface Length {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    boolean allowBlank() default false;

    String message() default "the length of str is not satisfy";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
