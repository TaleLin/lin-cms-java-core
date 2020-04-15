package io.github.talelin.autoconfigure.validator.impl;

import io.github.talelin.autoconfigure.validator.EqualField;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * 相等值校验器
 *
 * @author pedro@TaleLin
 */
public class EqualFieldValidator implements ConstraintValidator<EqualField, Object> {

    private String srcField;
    private String dstField;

    @Override
    public void initialize(EqualField constraintAnnotation) {
        this.srcField = constraintAnnotation.srcField();
        this.dstField = constraintAnnotation.dstField();
    }

    /**
     * 校验
     *
     * @param object  传入值
     * @param context 上下文
     * @return 是否成功
     */
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Class<?> clazz = object.getClass();

        Field srcField = ReflectionUtils.findField(clazz, this.srcField);
        Field dstField = ReflectionUtils.findField(clazz, this.dstField);
        try {
            srcField.setAccessible(true);
            dstField.setAccessible(true);
            String src = (String) srcField.get(object);
            String dst = (String) dstField.get(object);
            if (src.equals(dst))
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
