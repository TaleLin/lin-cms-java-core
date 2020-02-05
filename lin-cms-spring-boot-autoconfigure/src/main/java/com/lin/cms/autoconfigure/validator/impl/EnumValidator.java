package com.lin.cms.autoconfigure.validator.impl;

import com.lin.cms.autoconfigure.validator.Enum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

public class EnumValidator implements ConstraintValidator<Enum, Object> {

    private Class<?> cls; //枚举类

    private boolean allowNull;


    @Override
    public void initialize(Enum constraintAnnotation) {
        cls = constraintAnnotation.target();
        allowNull = constraintAnnotation.allowNull();
    }

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
