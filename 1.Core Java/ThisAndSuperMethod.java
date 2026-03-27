/* super()
class A {
    public A() {
        super(); // super must be the first statement in the constructor
        System.out.println("in A constructor");
    }

    public A(int a) {
        super();
        System.out.println("in A parameterized constructor");
    }
}

class B extends A {
    public B() {
        // super();
        super(5);
        System.out.println("in B constructor");
    }

    public B(int n) {
        // super() //call default constructor of super class
        super(n);
        System.out.println("in B parameterized constructor");

    }
}

public class ThisAndSuperMethod {
    public static void main(String[] args) {
        B obj = new B();
        // B obj1 = new B(5);
    }
}

*/


//this()
class A7{
    public A7(){
        super();
        System.out.println("in A constructor");
    }
    public A7(int n){
        super();
        System.out.println("in A parameterized constructor");
    }
}

class  B7 extends A7{
    public B7(){
        super();
        System.out.println("in B constructor");
    }

    public B7(int n){
        this(); //call constructor of same class
        System.out.println("in B parameterized constructor");
    }
}

public class ThisAndSuperMethod {
    public static void main(String[] args) {
        // B obj = new B();
        B7 obj1 = new B7(5);
    }
}

