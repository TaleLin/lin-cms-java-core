package io.github.talelin.autoconfigure.validator.impl;

import io.github.talelin.autoconfigure.validator.EqualField;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * EualFieldValidator 单元测试
 *
 * @author Juzi@TaleLin
 * @date 2020-05-25
 */
class EqualFieldValidatorTest {

    @EqualField(srcField = "src", dstField = "dst")
    static class TestDataString {
        public String src;
        public String dst;
    }

    @EqualField(srcField = "src", dstField = "dst")
    static class TestDataObject {
        public List<Integer> src;
        public List<Integer> dst;
    }

    /**
     * 校验 String 通过
     */
    @Test
    void testIsValidStringTrue() {
        TestDataString testDataString = new TestDataString();
        testDataString.src = "桔子";
        testDataString.dst = "桔子";
        Set<ConstraintViolation<TestDataString>> validate =
                Validation.buildDefaultValidatorFactory().getValidator().validate(testDataString);
        assertEquals(0, validate.size());
    }

    /**
     * 校验 String 不通过
     */
    @Test
    void testIsValidStringFalse() {
        TestDataString testDataString = new TestDataString();
        testDataString.src = "桔子";
        testDataString.dst = "橘子";
        Set<ConstraintViolation<TestDataString>> validate =
                Validation.buildDefaultValidatorFactory().getValidator().validate(testDataString);
        assertNotEquals(0, validate.size());
    }

    /**
     * 校验 Object 通过
     */
    @Test
    void testIsValidObjectTrue() {
        List<Integer> stringListA = new ArrayList<>();
        stringListA.add(1);
        List<Integer> stringListB = new ArrayList<>();
        stringListB.add(1);

        TestDataObject testDataObject = new TestDataObject();
        testDataObject.src = stringListA;
        testDataObject.dst = stringListB;

        Set<ConstraintViolation<TestDataObject>> validate =
                Validation.buildDefaultValidatorFactory().getValidator().validate(testDataObject);
        assertEquals(0, validate.size());
    }

    /**
     * 校验 Object 不通过
     */
    @Test
    void testIsValidObjectFalse() {
        List<Integer> stringListA = new ArrayList<>();
        stringListA.add(1);
        List<Integer> stringListB = new ArrayList<>();
        stringListB.add(2);

        TestDataObject testDataObject = new TestDataObject();
        testDataObject.src = stringListA;
        testDataObject.dst = stringListB;

        Set<ConstraintViolation<TestDataObject>> validate =
                Validation.buildDefaultValidatorFactory().getValidator().validate(testDataObject);
        assertNotEquals(0, validate.size());
    }

}