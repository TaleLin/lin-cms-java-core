package com.lin.cms.autoconfigure.validator.impl;

import com.lin.cms.autoconfigure.validator.LongList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * long 列表校验器
 */
public class LongListValidator implements ConstraintValidator<LongList, List<Long>> {

    private long min;

    private long max;

    private boolean allowBlank;

    @Override
    public void initialize(LongList constraintAnnotation) {
        this.max = constraintAnnotation.max();
        this.min = constraintAnnotation.min();
        this.allowBlank = constraintAnnotation.allowBlank();
    }

    /**
     * 校验
     *
     * @param value   传入值
     * @param context 上下文
     * @return 是否成功
     */
    @Override
    public boolean isValid(List<Long> value, ConstraintValidatorContext context) {
        if ((value == null || value.isEmpty())) {
            return allowBlank;
        }
        for (Long o : value) {
            if (o < min || o > max) {
                return false;
            }
        }
        return true;
    }
}
