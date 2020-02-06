package com.lin.cms.core.utils;

import com.lin.cms.core.annotation.Required;
import com.lin.cms.core.enums.UserLevel;

import java.lang.annotation.Annotation;

/**
 * 注解工具函数
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
