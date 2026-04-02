package com.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.learning.JavaCourse;

public class TestPurchaseCourse {
    @Test
    void testProceedWithCourse(){
        PurchaseCourse pc = new PurchaseCourse();
        boolean status = pc.proceedWithCourse(new JavaCourse());
        assertTrue(status);
    }

    @Test
    void test(){
        String str="Junit";
        assertFalse(str.length()<5);
    }
}
