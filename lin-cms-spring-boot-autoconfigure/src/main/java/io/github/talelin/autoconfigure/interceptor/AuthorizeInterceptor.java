package io.github.talelin.autoconfigure.interceptor;

import io.github.talelin.autoconfigure.interfaces.AuthorizeVerifyResolver;
import io.github.talelin.core.annotation.RouteMeta;
import io.github.talelin.core.enums.UserLevel;
import io.github.talelin.core.utils.AnnotationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 权限拦截器
 */
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AuthorizeVerifyResolver authorizeVerifyResolver;

    private String[] excludeMethods = new String[]{"OPTIONS"};

    public AuthorizeInterceptor() {
    }

    /**
     * 构造函数
     *
     * @param excludeMethods 不检查方法
     */
    public AuthorizeInterceptor(String[] excludeMethods) {
        this.excludeMethods = excludeMethods;
    }

    /**
     * 前置处理
     *
     * @param request  request 请求
     * @param response 相应
     * @param handler  处理器
     * @return 是否成功
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (checkInExclude(request.getMethod())) {
            // 有些请求方法无需检测，如OPTIONS
            return true;
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RouteMeta meta = method.getAnnotation(RouteMeta.class);
            // 考虑两种情况，1. 有 meta；2. 无 meta
            if (meta == null) {
                // 无meta的话，adminRequired和loginRequired
                return this.handleNoMeta(request, response, method);
            } else {
                // 有meta在权限范围之内，需要判断权限
                return this.handleMeta(request, response, method, meta);
            }
        } else {
            // handler不是HandlerMethod的情况
            return authorizeVerifyResolver.handleNotHandlerMethod(request, response, handler);
        }
    }

    private boolean handleNoMeta(HttpServletRequest request, HttpServletResponse response, Method method) {
        Annotation[] annotations = method.getAnnotations();
        UserLevel level = AnnotationUtil.findRequired(annotations);
        switch (level) {
            case LOGIN:
                // 登陆权限
                return authorizeVerifyResolver.handleLogin(request, response, null);
            case GROUP:
                // 分组权限
                return false;
            case ADMIN:
                // 管理员权限
                return authorizeVerifyResolver.handleAdmin(request, response, null);
            case REFRESH:
                // 刷新令牌
                return authorizeVerifyResolver.handleRefresh(request, response, null);
            default:
                return true;
        }
    }

    private boolean handleMeta(HttpServletRequest request, HttpServletResponse response, Method method, RouteMeta meta) {
        // 没有挂载到权限系统中，通过
        // 如果权限存在meta，可是却没有mount，则当作 no meta 处理
        if (!meta.mount()) {
            return this.handleNoMeta(request, response, method);
        }
        Annotation[] annotations = method.getAnnotations();
        UserLevel level = AnnotationUtil.findRequired(annotations);
        switch (level) {
            case LOGIN:
                // 登陆权限
                return authorizeVerifyResolver.handleLogin(request, response, meta);
            case GROUP:
                // 分组权限
                return authorizeVerifyResolver.handleGroup(request, response, meta);
            case ADMIN:
                // 管理员权限
                return authorizeVerifyResolver.handleAdmin(request, response, meta);
            case REFRESH:
                // 刷新令牌
                return authorizeVerifyResolver.handleRefresh(request, response, meta);
            default:
                return true;
        }
    }

    private boolean checkInExclude(String method) {
        for (String excludeMethod : excludeMethods) {
            if (method.equals(excludeMethod)) {
                return true;
            }
        }
        return false;
    }
}
