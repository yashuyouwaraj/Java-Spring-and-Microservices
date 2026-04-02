package com.performance;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import org.junit.jupiter.api.Test;

public class SortingArrayTest{
    @Test
    void testSortingMethod_Performance(){
        SortingArray array = new SortingArray();

        int unsorted[] = {2,5,1};

        assertTimeout(Duration.ofMillis(100),()->array.sortingArray(unsorted));
    }
}