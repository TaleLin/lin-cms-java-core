package com.lin.cms.core.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DateUtilTest {

    @Test
    public void getDurationDate() {
        Date durationDate = DateUtil.getDurationDate(10);
        long now = new Date().getTime();
        assertTrue(durationDate.getTime() > now);
        assertTrue(durationDate.getTime() - now > 5 * 1000);
        assertTrue(durationDate.getTime() - now <= 10 * 1000);
    }
}