package io.github.talelin.autoconfigure.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
public class ParameterExceptionTest {

    @Test
    public void test() {
        ParameterException exception = new ParameterException();
        String message = exception.getMessage();
        String localizedMessage = exception.getLocalizedMessage();
        int code = exception.getCode();
        assertEquals(10030, code);
        assertEquals("Parameters Error", message);
        assertEquals("Parameters Error", localizedMessage);
    }

    @Test
    public void test1() {
        ParameterException exception = new ParameterException("pedro犯了一个错误");
        String message = exception.getMessage();
        String localizedMessage = exception.getLocalizedMessage();
        int code = exception.getCode();
        assertEquals(10030, code);
        assertEquals("pedro犯了一个错误", message);
        assertEquals("pedro犯了一个错误", localizedMessage);
    }

    @Test
    public void test2() {
        Map<String, Object> errors = new HashMap<>();
        errors.put("nickname", "名称不能超过100字符");
        errors.put("age", "年龄不能为负数");
        ParameterException exception = new ParameterException(errors);
        String message = exception.getMessage();
        int code = exception.getCode();
        assertEquals(10030, code);
        assertEquals("{nickname=名称不能超过100字符, age=年龄不能为负数}", message);
    }

    @Test
    public void test3() {
        ParameterException exception = new ParameterException();
        exception.addError("nickname", "名称不能超过100字符");
        String message = exception.getMessage();
        int code = exception.getCode();
        assertEquals(10030, code);
        assertEquals("{nickname=名称不能超过100字符}", message);
    }

    @Test
    public void testNoArgsConstructor() {
        ParameterException exception = new ParameterException();
        int code = exception.getCode();
        boolean messageOnly = exception.isMessageOnly();
        assertEquals(10030, code);
        assertFalse(messageOnly);
    }

    @Test
    public void testMessageOnlyConstructor() {
        ParameterException exception = new ParameterException("参数错了吧");
        String message = exception.getMessage();
        boolean messageOnly = exception.isMessageOnly();
        assertEquals("参数错了吧", message);
        assertTrue(messageOnly);
    }

    @Test
    public void testAllArgsConstructor() {
        ParameterException exception = new ParameterException("参数错了吧", 10040);
        int code = exception.getCode();
        String message = exception.getMessage();
        boolean messageOnly = exception.isMessageOnly();
        assertEquals(10040, code);
        assertEquals("参数错了吧", message);
        assertFalse(messageOnly);
    }

    @Test
    public void testCodeOnlyConstructor() {
        ParameterException exception = new ParameterException(10040);
        int code = exception.getCode();
        String message = exception.getMessage();
        boolean messageOnly = exception.isMessageOnly();
        assertEquals(10040, code);
        assertEquals("Parameters Error", message);
        assertFalse(messageOnly);
    }

}