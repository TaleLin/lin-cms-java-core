package com.lin.cms.autoconfigure.validator.impl;

import com.lin.cms.autoconfigure.validator.DateTimeFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public class DateTimeValidator implements ConstraintValidator<DateTimeFormat, String> {

    private DateTimeFormat dateTimeFormat;

    @Override
    public void initialize(DateTimeFormat dateTime) {
        this.dateTimeFormat = dateTime;
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果value为null
        if (value == null) {
            return dateTimeFormat.allowNull();
        } else {
            String format = dateTimeFormat.format();
            if (value.length() != format.length()) {
                return false;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                simpleDateFormat.parse(value);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }
}
