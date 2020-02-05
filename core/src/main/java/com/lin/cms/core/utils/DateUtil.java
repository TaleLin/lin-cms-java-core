package com.lin.cms.core.utils;

import java.util.Date;

public class DateUtil {

    /**
     * 获得过期时间
     *
     * @param duration 延时时间，单位s
     * @return Date
     */
    public static Date getDurationDate(long duration) {
        long expireTime = System.currentTimeMillis() + duration * 1000;
        return new Date(expireTime);
    }
}
