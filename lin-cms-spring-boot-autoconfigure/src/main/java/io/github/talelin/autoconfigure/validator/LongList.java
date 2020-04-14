package io.github.talelin.autoconfigure.validator;

import javax.validation.Payload;

/**
 * 整型列表校验，校验 List Long 类型
 *
 * @author pedro@TaleLin
 */
public @interface LongList {

    /**
     * 校验信息
     *
     * @return 返回校验信息
     */
    String message() default "Integer list cannot can't be blank";

    /**
     * 每一个整数的最小值
     *
     * @return 最小值
     */
    long min() default 0;

    /**
     * 每一个整数的最大值
     *
     * @return 最大值
     */
    long max() default Long.MAX_VALUE;

    /**
     * 允许链表为 NULL 和 size == 0
     *
     * @return 是否可以为空
     */
    boolean allowBlank() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
