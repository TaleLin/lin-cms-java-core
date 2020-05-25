package io.github.talelin.autoconfigure.validator.impl;

import io.github.talelin.autoconfigure.validator.EqualField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.reflect.Field;

/**
 * 相等值校验器
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class EqualFieldValidator implements ConstraintValidator<EqualField, Object> {

    private static final Logger log = LoggerFactory.getLogger(EqualFieldValidator.class);

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

            if (srcField == null || dstField == null) {
                throw new ValidationException("反射获取变量失败");
            }

            srcField.setAccessible(true);
            dstField.setAccessible(true);
            Object src = srcField.get(object);
            Object dst = dstField.get(object);

            // 其中一个变量为 null 时，则必须两个都为 null 才相等
            if (src == null || dst == null) {
                return src == dst;
            }

            // 如果两个对象内存地址相同，则一定相等
            if (src == dst) {
                return true;
            }

            // 调用 equals 方法比较
            return src.equals(dst);
        } catch (Exception e) {
            log.warn("EqualFieldValidator 校验异常", e);
            return false;
        }
    }
}
