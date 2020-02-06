package io.github.talelin.autoconfigure.validator;

import io.github.talelin.autoconfigure.validator.impl.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验枚举，传入的值必须为target(枚举类)中的一项
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValidator.class})
@Documented
public @interface Enum {

    String message() default "value must in enum";

    boolean allowNull() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> target();
}
