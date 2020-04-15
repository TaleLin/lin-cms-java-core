package io.github.talelin.core.util;

import io.github.talelin.core.annotation.*;
import io.github.talelin.core.enumeration.UserLevel;

import java.lang.annotation.Annotation;

/**
 * 注解工具函数
 *
 * @author pedro@TaleLin
 */
public class AnnotationUtil {

    /***
     * 得到用户等级
     * @param annotations 注解
     * @return 用户等级
     */
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
