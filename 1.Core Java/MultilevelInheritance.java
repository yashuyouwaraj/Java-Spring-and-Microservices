class VeryAdvCalc extends AdvCalc{
    public int mod(int a, int b){
        return a%b;
    }
    public int pow(int a, int b){
        return (int) Math.pow(a, b);
    }
}

// Multilevel Inheritance: A class can inherit from a derived class, thus forming a multilevel inheritance. In this example, VeryAdvCalc is derived from AdvCalc, which is derived from Calc.
// In this case, VeryAdvCalc inherits all the methods from both AdvCalc and Calc, allowing it to perform addition, subtraction, multiplication, division, modulus, and exponentiation operations.

public class MultilevelInheritance {
    public static void main(String[] args) {
        VeryAdvCalc c = new VeryAdvCalc();
        System.out.println(c.add(10, 20));
        System.out.println(c.sub(20, 10));
        System.out.println(c.mul(10, 20));
        System.out.println(c.div(50, 10));
        System.out.println(c.mod(50, 8));
        System.out.println(c.pow(2, 3));
    }
}
