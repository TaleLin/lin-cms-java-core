package io.github.talelin.autoconfigure.bean;

import io.github.talelin.core.annotation.AdminMeta;
import io.github.talelin.core.annotation.GroupMeta;
import io.github.talelin.core.annotation.LoginMeta;
import io.github.talelin.core.annotation.RouteMeta;
import io.github.talelin.core.enumeration.UserLevel;
import io.github.talelin.core.util.AnnotationUtil;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由信息收集器
 */
public class RouteMetaCollector implements BeanPostProcessor {

    private Map<String, MetaInfo> metaMap = new ConcurrentHashMap<>();

    private Map<String, Map<String, Set<String>>> structuralMeta = new ConcurrentHashMap<>();

    public RouteMetaCollector() {
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 扫描注解信息，并提取
     *
     * @param bean     spring bean
     * @param beanName 名称
     * @return spring bean
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : methods) {
            AdminMeta adminMeta = AnnotationUtils.findAnnotation(method, AdminMeta.class);
            if (adminMeta != null && adminMeta.mount()) {
                putOneMetaInfo(method, adminMeta.permission(), adminMeta.module(), UserLevel.ADMIN);
                continue;
            }
            GroupMeta groupMeta = AnnotationUtils.findAnnotation(method, GroupMeta.class);
            if (groupMeta != null && groupMeta.mount()) {
                putOneMetaInfo(method, groupMeta.permission(), groupMeta.module(), UserLevel.GROUP);
                continue;
            }
            LoginMeta loginMeta = AnnotationUtils.findAnnotation(method, LoginMeta.class);
            if (loginMeta != null && loginMeta.mount()) {
                putOneMetaInfo(method, loginMeta.permission(), loginMeta.module(), UserLevel.LOGIN);
                continue;
            }
            // 最后寻找 RouteMeta
            RouteMeta routeMeta = AnnotationUtils.findAnnotation(method, RouteMeta.class);
            if (routeMeta != null && routeMeta.mount()) {
                UserLevel level = AnnotationUtil.findRequired(method.getAnnotations());
                putOneMetaInfo(method, routeMeta.permission(), routeMeta.module(), level);
            }
        }
        return bean;
    }

    private void putOneMetaInfo(Method method, String permission, String module, UserLevel userLevel) {
        String methodName = method.getName();
        String className = method.getDeclaringClass().getName();
        String identity = className + "#" + methodName;
        MetaInfo metaInfo = new MetaInfo(permission, module, identity, userLevel);
        metaMap.put(identity, metaInfo);
        this.putMetaIntoStructuralMeta(identity, metaInfo);
    }

    private void putMetaIntoStructuralMeta(String identity, MetaInfo meta) {
        String module = meta.getModule();
        String permission = meta.getPermission();
        // 如果已经存在了该 module，直接向里面增加
        if (structuralMeta.containsKey(module)) {
            Map<String, Set<String>> moduleMap = structuralMeta.get(module);
            // 如果 permission 已经存在
            this.putIntoModuleMap(moduleMap, identity, permission);
        } else {
            // 不存在 该 module，创建该 module
            Map<String, Set<String>> moduleMap = new HashMap<>();
            // 如果 permission 已经存在
            this.putIntoModuleMap(moduleMap, identity, permission);
            structuralMeta.put(module, moduleMap);
        }
    }

    private void putIntoModuleMap(Map<String, Set<String>> moduleMap, String identity, String auth) {
        if (moduleMap.containsKey(auth)) {
            moduleMap.get(auth).add(identity);
        } else {
            Set<String> eps = new HashSet<>();
            eps.add(identity);
            moduleMap.put(auth, eps);
        }
    }

    /**
     * 获取路由信息map
     *
     * @return 路由信息map
     */
    public Map<String, MetaInfo> getMetaMap() {
        return metaMap;
    }

    public MetaInfo findMeta(String key) {
        return metaMap.get(key);
    }

    public MetaInfo findMetaByPermission(String permission) {
        Collection<MetaInfo> values = metaMap.values();
        MetaInfo[] objects = values.toArray(new MetaInfo[0]);
        for (int i = 0; i < objects.length; i++) {
            if (objects[i].getPermission().equals(permission)) {
                return objects[i];
            }
        }
        return null;
    }

    public void setMetaMap(Map<String, MetaInfo> metaMap) {
        this.metaMap = metaMap;
    }

    /**
     * 获得结构化路由信息
     *
     * @return 路由信息
     */
    public Map<String, Map<String, Set<String>>> getStructuralMeta() {
        return structuralMeta;
    }

    public void setStructrualMeta(Map<String, Map<String, Set<String>>> structuralMeta) {
        this.structuralMeta = structuralMeta;
    }
}
