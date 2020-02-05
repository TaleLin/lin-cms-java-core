package com.lin.cms.autoconfigure.validator.impl;

import com.lin.cms.autoconfigure.validator.NotEmptyFields;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NotEmptyFieldsValidator implements ConstraintValidator<NotEmptyFields, List<String>> {

    private boolean allowNull;

    private boolean allowEmpty;

    @Override
    public void initialize(NotEmptyFields constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }

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
