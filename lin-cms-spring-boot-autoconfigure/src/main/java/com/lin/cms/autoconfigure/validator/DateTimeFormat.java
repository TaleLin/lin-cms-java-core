package com.lin.cms.autoconfigure.validator;

import javax.validation.Payload;

/**
 * 日期格式校验器 默认校验日期格式 yyyy-MM-dd HH:mm:ss
 * 可通过指定 format 来更改校验格式
 */
public @interface DateTimeFormat {

    String message() default "date format invalid";

    String format() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 是否允许传入空的日期，如果为false，当传入空字符串时，校验失败
     *
     * @return 是否可以为空
     */
    boolean allowNull() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
