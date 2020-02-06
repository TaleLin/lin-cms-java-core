package com.lin.cms.autoconfigure.validator;

import com.lin.cms.autoconfigure.validator.impl.EqualFieldValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字段相等
 */
@Target({TYPE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EqualFieldValidator.class})
public @interface EqualField {

    String srcField() default "";

    String dstField() default "";

    String message() default "the two fields must be equal";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
