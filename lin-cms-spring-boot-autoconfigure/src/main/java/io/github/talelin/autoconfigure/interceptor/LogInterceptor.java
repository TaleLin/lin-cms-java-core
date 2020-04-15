package io.github.talelin.autoconfigure.interceptor;

import io.github.talelin.core.annotation.Logger;
import io.github.talelin.core.annotation.RouteMeta;
import io.github.talelin.autoconfigure.interfaces.LoggerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 行为日志拦截器
 *
 * @author pedro@TaleLin
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private LoggerResolver loggerResolver;

    /**
     * 后置处理
     *
     * @param request      请求
     * @param response     响应
     * @param handler      处理器
     * @param modelAndView 视图
     * @throws Exception 异常
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 记录日志
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Logger logger = method.getAnnotation(Logger.class);
            RouteMeta meta = method.getAnnotation(RouteMeta.class);
            if (logger != null) {
                // parse template and extract properties from request,response and modelAndView
                loggerResolver.handle(meta, logger, request, response);
            }
        }
        super.postHandle(request, response, handler, modelAndView);
    }
}
