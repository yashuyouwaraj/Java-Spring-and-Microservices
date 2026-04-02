package com.jnitsample;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShapesTest {

    ShapesTest(){
        System.out.println("Test obj is created before test method");
    };

    @BeforeAll
    void beforeall(){
        System.out.println("Before all test");
    }
    
    @AfterAll
    void afterall(){
        System.out.println("After all test cases");
    }

    Shapes shape;

    @BeforeEach
    void init() {
        shape = new Shapes();
        System.err.println("Before every method");
    }

    @Test
    void test() {
        assertEquals(6, 6);
    }

    @Test
    void testComputeSquareArea() {
        assertEquals(25, shape.ComputeSquareArea(5), "Square area should be 25");
    }

    @Test
    void testComputeCircleArea() {
        assertEquals(Math.PI * 4 * 4, shape.ComputeCircleArea(4), "Circle area should be 16*PI");
    }

    @Test
    void testComputeSquareArea1() {
        assertNotEquals(5674, shape.ComputeSquareArea(24));
    }

    @Test
    void testComputeSquareArea_WithMessage() {
        assertNotEquals(5674, shape.ComputeSquareArea(24), "Square area should not be 5674");
    }

    @Test
    void testComputeSquareArea_Supplier() {
        assertNotEquals(5674, shape.ComputeSquareArea(24), "Square area should not be 5674");
    }

    @AfterEach
    void destory() {
        System.out.println("After test clean up");
    }

    
}
