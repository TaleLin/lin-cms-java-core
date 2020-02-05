package com.lin.cms.core.enums;

public enum UserLevel {

    TOURIST, // 游客即可访问

    LOGIN, // 登录才可访问

    GROUP, // 登录有权限才可访问

    ADMIN, // 管理员权限

    REFRESH // 令牌刷新
}
