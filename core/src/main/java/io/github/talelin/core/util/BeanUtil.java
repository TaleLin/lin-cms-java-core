package io.github.talelin.core.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Bean 工具函数
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class BeanUtil {

    private static final String SET_PREFIX = "set";
    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";

    /**
     * 获得类的属性
     *
     * @param clazz        类
     * @param propertyName 属性名
     * @return 属性值
     */
    public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) {
        //根据需求，定制 自己的get和set方法
        Method setMethod;
        Method getMethod;
        PropertyDescriptor pd = null;
        try {
            // 根据字段名来获取字段
            Field field = clazz.getDeclaredField(propertyName);
            // 构建方法的后缀
            String methodEnd = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            setMethod = clazz.getDeclaredMethod(SET_PREFIX + methodEnd, field.getType());
            // 构建get 方法
            getMethod = clazz.getDeclaredMethod(GET_PREFIX + methodEnd);
            // 构建一个属性描述器 把对应属性 propertyName 的 get 和 set 方法保存到属性描述器中
            pd = new PropertyDescriptor(propertyName, getMethod, setMethod);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return pd;
    }

    /**
     * 获得类的属性2
     *
     * @param clazz        类
     * @param propertyName 属性名
     * @return 属性值
     */
    public static PropertyDescriptor getPropertyDescriptor2(Class<?> clazz, String propertyName) {
        //使用 PropertyDescriptor 提供的 get和set方法
        try {
            return new PropertyDescriptor(propertyName, clazz);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置对象属性
     *
     * @param obj          对象
     * @param propertyName 属性名
     * @param value        属性值
     */
    public static void setProperty(Object obj, String propertyName, Object value) {
        // 获取对象的类型
        Class<?> clazz = obj.getClass();
        // 获取 clazz 类型中的 propertyName 的属性描述器
        PropertyDescriptor pd = getPropertyDescriptor(clazz, propertyName);
        // 从属性描述器中获取 set 方法
        Method setMethod = pd.getWriteMethod();
        try {
            // 调用 set 方法将传入的value值保存属性中去
            setMethod.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得对象的属性
     *
     * @param obj          对象
     * @param propertyName 属性名
     * @return 属性值
     */
    public static String getProperty(Object obj, String propertyName) {
        // 获取对象的类型
        Class<?> clazz = obj.getClass();
        String value;
        try {
            // 获取
            PropertyDescriptor pd = getPropertyDescriptor(clazz, propertyName);
            // 从属性描述器中获取 get 方法
            Method getMethod = pd.getReadMethod();
            // 调用方法获取方法的返回值
            value = getMethod.invoke(clazz).toString();
        } catch (Exception e) {
            return "";
        }
        // 返回值
        return value;
    }

    /**
     * 获得string属性值
     *
     * @param obj      对象
     * @param propName 属性名
     * @return 属性值
     */
    public static String getValueByPropName(Object obj, String propName) {
        // 获取对象的类型
        Class<?> clazz = obj.getClass();
        String value;
        try {
            String upperCaseFirstOne = toUpperCaseFirstOne(propName);
            Method method = clazz.getMethod(GET_PREFIX + upperCaseFirstOne);
            Object result = method.invoke(obj);
            value = result.toString();
        } catch (Exception e) {
            // 如果异常，或者没有返回空字符串
            return "";
        }
        return value;
    }

    /**
     * 字符串首字母大写
     *
     * @param s 字符串
     * @return 处理后字符串
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }
    }
}
