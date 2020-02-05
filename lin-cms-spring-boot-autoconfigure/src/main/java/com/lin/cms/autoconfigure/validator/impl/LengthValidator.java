package com.lin.cms.autoconfigure.validator.impl;

import com.lin.cms.autoconfigure.validator.Length;
import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
