package com.lin.cms.core.utils;

import com.lin.cms.core.annotation.Required;
import com.lin.cms.core.enums.UserLevel;

import java.lang.annotation.Annotation;

public class AnnotationUtil {

    public static UserLevel findRequired(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            Required required = aClass.getAnnotation(Required.class);
            if (required != null) {
                return required.level();
            }
        }
        return UserLevel.TOURIST;
    }
}
