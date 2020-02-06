package io.github.talelin.autoconfigure.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具类
 */
public class RequestUtil {

    /**
     * 获得当前请求
     *
     * @return 请求
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获得请求 url
     *
     * @return url
     */
    public static String getRequestUrl() {
        return getRequest().getServletPath();
    }

    /**
     * 获得请求简略信息
     *
     * @param request 请求
     * @return 简略信息
     */
    public static String getSimpleRequest(HttpServletRequest request) {
        return String.format("%s %s", request.getMethod(), request.getServletPath());
    }

    /**
     * 获得请求简略信息
     *
     * @return 简略信息
     */
    public static String getSimpleRequest() {
        HttpServletRequest request = getRequest();
        return String.format("%s %s", request.getMethod(), request.getServletPath());
    }
}
