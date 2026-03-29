/*
enum is a special data type in Java that represents a group of constants. It is used to define a collection of named values, which can be used to represent a fixed set of options or categories. Enums are often used to improve code readability and maintainability by providing meaningful names for constant values.
*/
enum Status1 {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

public class WhatisEnum {
    public static void main(String[] args) {
        Status1 s = Status1.IN_PROGRESS;
        System.out.println("Current status: " + s);

        System.out.println(s.ordinal()); // returns the position of the enum constant (0-based index)

        Status1[] ss = Status1.values();
        for (Status1 s1 : ss) {
            System.out.println(s1+" at index " + s1.ordinal());
        }
    }
}
