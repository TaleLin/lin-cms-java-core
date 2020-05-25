package io.github.talelin.autoconfigure.validator.impl;

import io.github.talelin.autoconfigure.validator.DateTimeFormat;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * DateTimeFormatValidator 单元测试
 *
 * @author Juzi@TaleLin
 * @date 2020-05-25
 */
class DateTimeFormatValidatorTest {
    static class TestData {
        @DateTimeFormat
        public String datetime;
    }

    /**
     * 测试校验通过
     */
    @Test
    public void testIsValidTrue() {
        TestData testData = new TestData();
        testData.datetime = "2020-05-25 19:37:48";

        Set<ConstraintViolation<TestData>> validate =
                Validation.buildDefaultValidatorFactory().getValidator().validate(testData);
        assertEquals(0, validate.size());
    }

    /**
     * 测试校验不通过
     */
    @Test
    public void testIsValidFalse() {
        TestData testData = new TestData();
        testData.datetime = "2020/05/25 19:37:48";

        Set<ConstraintViolation<TestData>> validate =
                Validation.buildDefaultValidatorFactory().getValidator().validate(testData);
        assertNotEquals(0, validate.size());
    }
}