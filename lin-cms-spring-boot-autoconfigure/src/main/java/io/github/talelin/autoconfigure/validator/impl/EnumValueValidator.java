package io.github.talelin.autoconfigure.validator.impl;

import io.github.talelin.autoconfigure.validator.EnumValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

/**
 * 枚举校验器
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private static final Logger log = LoggerFactory.getLogger(EnumValueValidator.class);

    /**
     * 枚举类
     */
    private Class<?> cls;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        cls = constraintAnnotation.target();
    }

    /**
     * 校验
     *
     * @param value   传入值
     * @param context 上下文
     * @return 是否成功
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            Object[] objs = cls.getEnumConstants();
            Method method = cls.getMethod("getValue");
            for (Object obj : objs) {
                Object val = method.invoke(obj);
                if (val.equals(value)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            log.warn("EnumValue 校验异常", e);
            return false;
        }
    }
}
