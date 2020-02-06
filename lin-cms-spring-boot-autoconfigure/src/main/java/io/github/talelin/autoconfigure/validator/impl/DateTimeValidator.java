package io.github.talelin.autoconfigure.validator.impl;

import io.github.talelin.autoconfigure.validator.DateTimeFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

/**
 * 日期校验器实现
 */
public class DateTimeValidator implements ConstraintValidator<DateTimeFormat, String> {

    private DateTimeFormat dateTimeFormat;

    @Override
    public void initialize(DateTimeFormat dateTime) {
        this.dateTimeFormat = dateTime;
    }

    /**
     * 校验
     *
     * @param value   传入数据
     * @param context 上下文
     * @return 是否成功
     */
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
