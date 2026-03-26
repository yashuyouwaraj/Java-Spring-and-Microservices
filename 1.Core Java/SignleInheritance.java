class Calc {
    public int add(int a, int b){
        return a+b;
    }
    public int sub(int a, int b){
        return a-b;
    }
}

class AdvCalc extends Calc{
    public int mul(int a, int b){
        return a*b;
    }
    public int div(int a, int b){
        return a/b;
    }
}

public class SignleInheritance {
    public static void main(String[] args) {
        AdvCalc c = new AdvCalc();
        System.out.println(c.add(10, 20));
        System.out.println(c.sub(20, 10));
        System.out.println(c.mul(10, 20));
        System.out.println(c.div(50, 10));
    }
}

// Single Inheritance: A class can inherit from another class, thus forming a single inheritance. In this example, AdvCalc is derived from Calc, which means that AdvCalc inherits all the methods of Calc. This allows us to use the add and sub methods of Calc in the AdvCalc class, along with its own mul and div methods.