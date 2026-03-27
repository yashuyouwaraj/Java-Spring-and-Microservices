public class WrapperClass {
    public static void main(String[] args) {
        int num = 10;
        // Boxing: Converting primitive int to Integer object

        //Integer num1=new Integer(num);     //boxing

        Integer boxedNum= num; // Automatic boxing

        int num2= num; // Unboxing: Converting Integer object back to primitive int
        System.out.println("Boxed Integer: " + boxedNum);
        System.out.println("Unboxed int: " + num2);

        String str = "123";
        int num3 = Integer.parseInt(str); // Unboxing: Converting String to primitive int
        System.out.println("Unboxed int from String: " + num3);
    }
}
