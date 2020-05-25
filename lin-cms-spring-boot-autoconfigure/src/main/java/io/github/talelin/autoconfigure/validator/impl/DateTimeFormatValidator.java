package io.github.talelin.autoconfigure.validator.impl;

import io.github.talelin.autoconfigure.validator.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

/**
 * 日期校验器实现
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class DateTimeFormatValidator implements ConstraintValidator<DateTimeFormat, String> {

    private static final Logger log = LoggerFactory.getLogger(DateTimeFormatValidator.class);

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
        // value 为 null，校验通过
        if (value == null) {
            return true;
        }

        String format = dateTimeFormat.pattern();

        // 长度不同，则肯定格式不匹配，校验不通过
        if (value.length() != format.length()) {
            return false;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            simpleDateFormat.parse(value);
            return true;
        } catch (Exception e) {
            log.warn("DateTimeFormat 校验异常", e);
            return false;
        }
    }
}
