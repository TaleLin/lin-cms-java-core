package com.lin.cms.autoconfigure.interfaces;

import com.lin.cms.core.annotation.RouteMeta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthorizeVerifyResolver {

    /**
     * 处理 LoginRequired的情况
     */
    boolean handleLogin(HttpServletRequest request, HttpServletResponse response, RouteMeta meta);

    /**
     * 处理 GroupRequired的情况
     */
    boolean handleGroup(HttpServletRequest request, HttpServletResponse response, RouteMeta meta);

    /**
     * 处理 AdminRequired的情况
     */
    boolean handleAdmin(HttpServletRequest request, HttpServletResponse response, RouteMeta meta);

    /**
     * 处理 RefreshRequired的情况
     */
    boolean handleRefresh(HttpServletRequest request, HttpServletResponse response, RouteMeta meta);

    /**
     * 处理 当前的handler 不是 HandlerMethod 的情况
     */
    boolean handleNotHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler);
}
