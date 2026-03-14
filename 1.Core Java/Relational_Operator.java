public class Relational_Operator {
    public static void main(String[] args) {
        int num1 = 10;
        int num2 = 20;

        // Using relational operators to compare num1 and num2
        System.out.println("num1 > num2: " + (num1 > num2)); // false
        System.out.println("num1 < num2: " + (num1 < num2)); // true
        System.out.println("num1 >= num2: " + (num1 >= num2)); // false
        System.out.println("num1 <= num2: " + (num1 <= num2)); // true
        System.out.println("num1 == num2: " + (num1 == num2)); // false
        System.out.println("num1 != num2: " + (num1 != num2)); // true

        // Comparing with another value
        int num3 = 10;
        System.out.println("num1 == num3: " + (num1 == num3)); // true
    }
}
