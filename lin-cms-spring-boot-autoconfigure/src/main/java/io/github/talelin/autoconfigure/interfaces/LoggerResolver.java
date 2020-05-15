package io.github.talelin.autoconfigure.interfaces;

import io.github.talelin.core.annotation.Logger;
import io.github.talelin.core.annotation.PermissionMeta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 行为日志记录
 *
 * @author pedro@TaleLin
 */
public interface LoggerResolver {

    /**
     * 处理
     *
     * @param meta     路由信息
     * @param logger   logger 信息
     * @param request  请求
     * @param response 响应
     */
    void handle(PermissionMeta meta, Logger logger, HttpServletRequest request, HttpServletResponse response);
}
