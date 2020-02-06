package com.lin.cms.autoconfigure.beans;

import com.lin.cms.core.annotation.RouteMeta;
import com.lin.cms.autoconfigure.interfaces.MetaPreHandler;
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

    private Map<String, RouteMeta> metaMap = new ConcurrentHashMap<>();

    private Map<String, Map<String, Set<String>>> structuralMeta = new ConcurrentHashMap<>();

    private MetaPreHandler preHandler;

    public RouteMetaCollector() {
    }

    public RouteMetaCollector(MetaPreHandler preHandler) {
        this.preHandler = preHandler;
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
            RouteMeta meta = AnnotationUtils.findAnnotation(method, RouteMeta.class);
            if (meta != null) {
                if (preHandler != null) {
                    preHandler.handle(meta);
                }
                if (meta.mount()) {
                    String methodName = method.getName();
                    String className = method.getDeclaringClass().getName();
                    String identity = className + "#" + methodName;
                    metaMap.put(identity, meta);
                    this.putMetaIntoStructuralMeta(identity, meta);
                }
            }
        }
        return bean;
    }

    private void putMetaIntoStructuralMeta(String identity, RouteMeta meta) {
        String module = meta.module();
        String permission = meta.permission();
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
    public Map<String, RouteMeta> getMetaMap() {
        return metaMap;
    }

    public RouteMeta findMeta(String key) {
        return metaMap.get(key);
    }

    public RouteMeta findMetaByPermission(String permission) {
        Collection<RouteMeta> values = metaMap.values();
        RouteMeta[] objects = values.toArray(new RouteMeta[0]);
        for (int i = 0; i < objects.length; i++) {
            if (objects[i].permission().equals(permission)) {
                return objects[i];
            }
        }
        return null;
    }

    public void setMetaMap(Map<String, RouteMeta> metaMap) {
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
