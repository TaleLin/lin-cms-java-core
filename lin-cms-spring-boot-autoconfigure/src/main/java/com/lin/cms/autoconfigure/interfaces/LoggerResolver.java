package com.lin.cms.autoconfigure.interfaces;

import com.lin.cms.core.annotation.Logger;
import com.lin.cms.core.annotation.RouteMeta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 行为日志记录
 */
public interface LoggerResolver {

    void handle(RouteMeta meta, Logger logger, HttpServletRequest request, HttpServletResponse response);
}
