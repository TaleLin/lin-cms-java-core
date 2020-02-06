package com.lin.cms.autoconfigure.validator.impl;

import com.lin.cms.autoconfigure.validator.Length;
import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符串长度校验器
 */
public class LengthValidator implements ConstraintValidator<Length, String> {

    private boolean allowBlank;

    private int min;

    private int max;

    @Override
    public void initialize(Length constraintAnnotation) {
        this.allowBlank = constraintAnnotation.allowBlank();
        this.max = constraintAnnotation.max();
        this.min = constraintAnnotation.min();
    }

    /**
     * 校验
     *
     * @param value   传入值
     * @param context 上下文
     * @return 是否成功
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (allowBlank) {
            if (Strings.isBlank(value)) {
                return true;
            }
        } else {
            if (value.length() >= min && value.length() <= max) {
                return true;
            }
        }
        return false;
    }
}
