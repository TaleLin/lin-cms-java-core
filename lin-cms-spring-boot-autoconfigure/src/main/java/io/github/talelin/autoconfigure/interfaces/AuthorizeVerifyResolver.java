package io.github.talelin.autoconfigure.interfaces;

import io.github.talelin.core.annotation.RouteMeta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限认证接口
 */
public interface AuthorizeVerifyResolver {


    /**
     * 处理 LoginRequired的情况
     *
     * @param request  请求
     * @param response 响应
     * @param meta     路由信息
     * @return 是否成功
     */
    boolean handleLogin(HttpServletRequest request, HttpServletResponse response, RouteMeta meta);

    /**
     * 处理 GroupRequired的情况
     *
     * @param request  请求
     * @param response 响应
     * @param meta     路由信息
     * @return 是否成功
     */
    boolean handleGroup(HttpServletRequest request, HttpServletResponse response, RouteMeta meta);

    /**
     * 处理 AdminRequired的情况
     *
     * @param request  请求
     * @param response 响应
     * @param meta     路由信息
     * @return 是否成功
     */
    boolean handleAdmin(HttpServletRequest request, HttpServletResponse response, RouteMeta meta);

    /**
     * 处理 RefreshRequired的情况
     *
     * @param request  请求
     * @param response 响应
     * @param meta     路由信息
     * @return 是否成功
     */
    boolean handleRefresh(HttpServletRequest request, HttpServletResponse response, RouteMeta meta);

    /**
     * 处理 当前的handler 不是 HandlerMethod 的情况
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return 是否成功
     */
    boolean handleNotHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler);
}
