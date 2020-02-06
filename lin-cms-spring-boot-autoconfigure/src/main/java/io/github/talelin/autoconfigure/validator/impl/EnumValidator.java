package io.github.talelin.autoconfigure.validator.impl;

import io.github.talelin.autoconfigure.validator.Enum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

/**
 * 枚举校验器
 */
public class EnumValidator implements ConstraintValidator<Enum, Object> {

    private Class<?> cls; //枚举类

    private boolean allowNull;


    @Override
    public void initialize(Enum constraintAnnotation) {
        cls = constraintAnnotation.target();
        allowNull = constraintAnnotation.allowNull();
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
        if (value == null && allowNull) {
            return true;
        }
        if (cls.isEnum()) {
            Object[] objs = cls.getEnumConstants();
            try {
                Method method = cls.getMethod("getValue");
                for (Object obj : objs) {
                    Object val = method.invoke(obj);
                    if (val.equals(value)) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
