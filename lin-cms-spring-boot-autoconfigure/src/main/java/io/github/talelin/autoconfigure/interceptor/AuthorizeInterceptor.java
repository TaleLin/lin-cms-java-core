package io.github.talelin.autoconfigure.interceptor;

import io.github.talelin.autoconfigure.bean.MetaInfo;
import io.github.talelin.autoconfigure.bean.PermissionMetaCollector;
import io.github.talelin.autoconfigure.interfaces.AuthorizeVerifyResolver;
import io.github.talelin.core.enumeration.UserLevel;
import io.github.talelin.core.util.AnnotationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 权限拦截器
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AuthorizeVerifyResolver authorizeVerifyResolver;

    @Autowired
    private PermissionMetaCollector permissionMetaCollector;

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
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (checkInExclude(request.getMethod())) {
            // 有些请求方法无需检测，如OPTIONS
            return true;
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            String methodName = method.getName();
            String className = method.getDeclaringClass().getName();
            String identity = className + "#" + methodName;
            MetaInfo meta = permissionMetaCollector.findMeta(identity);
            // AdminMeta adminMeta = method.getAnnotation(AdminMeta.class);
            // PermissionMeta meta = method.getAnnotation(PermissionMeta.class);
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

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        authorizeVerifyResolver.handlePostHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        authorizeVerifyResolver.handleAfterCompletion(request, response, handler, ex);
    }

    private boolean handleNoMeta(HttpServletRequest request, HttpServletResponse response, Method method) {
        Annotation[] annotations = method.getAnnotations();
        UserLevel level = AnnotationUtil.findRequired(annotations);
        switch (level) {
            case LOGIN:
            case GROUP:
                // 分组权限
                // 登陆权限
                return authorizeVerifyResolver.handleLogin(request, response, null);
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

    private boolean handleMeta(HttpServletRequest request, HttpServletResponse response, Method method, MetaInfo meta) {
        // Annotation[] annotations = method.getAnnotations();
        // UserLevel level = AnnotationUtil.findRequired(annotations);
        UserLevel level = meta.getUserLevel();
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
