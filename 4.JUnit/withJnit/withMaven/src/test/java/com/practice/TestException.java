package com.practice;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class TestException{
    @Test
    void testSortingArray_Exception(){
        try{
            SortingArray s = new SortingArray();
        int unsorted [] = null;
        int sortedArray[] = s.sortingArray(unsorted);

        for(int ele: sortedArray){
            System.out.print(ele+" ");
        }
        System.out.println("Sorting array is successful");
        fail("Sorting array is failed");
        } catch(NullPointerException e){
            System.out.println("Null pointer exception is handled");
        }
    }

    @Test
    void testSortingArray_Exception2(){
        SortingArray s = new SortingArray();
        int unsorted [] = null;
        assertThrows(NullPointerException.class, ()-> s.sortingArray(unsorted));
        
    }
}