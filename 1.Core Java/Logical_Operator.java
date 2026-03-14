public class Logical_Operator {
    public static void main(String[] args) {
        boolean a = true;
        boolean b = false;

        // Logical AND (&&) operator
        System.out.println("a && b: " + (a && b)); // false

        // Logical OR (||) operator
        System.out.println("a || b: " + (a || b)); // true

        // Logical NOT (!) operator
        System.out.println("!a: " + (!a)); // false
        System.out.println("!b: " + (!b)); // true

        // Combining logical operators
        System.out.println("(a && b) || (!a && !b): " + ((a && b) || (!a && !b))); // false
    }
}
