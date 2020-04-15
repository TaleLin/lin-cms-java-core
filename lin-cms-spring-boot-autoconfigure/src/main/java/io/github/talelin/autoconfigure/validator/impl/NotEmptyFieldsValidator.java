package io.github.talelin.autoconfigure.validator.impl;

import io.github.talelin.autoconfigure.validator.NotEmptyFields;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * 非空校验器
 *
 * @author pedro@TaleLin
 */
public class NotEmptyFieldsValidator implements ConstraintValidator<NotEmptyFields, List<String>> {

    private boolean allowNull;

    private boolean allowEmpty;

    @Override
    public void initialize(NotEmptyFields constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }

    /**
     * 校验
     *
     * @param objects 传入值
     * @param context 上下文
     * @return 是否成功
     */
    @Override
    public boolean isValid(List<String> objects, ConstraintValidatorContext context) {
        if (allowNull && objects == null) {
            return true;
        }
        if (allowEmpty && objects.size() == 0) {
            return true;
        }
        return objects.stream().allMatch(nef -> nef != null && !nef.trim().isEmpty());
    }
}
