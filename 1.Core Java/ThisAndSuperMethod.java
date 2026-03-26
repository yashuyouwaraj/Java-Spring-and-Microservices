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
class A{
    public A(){
        super();
        System.out.println("in A constructor");
    }
    public A(int n){
        super();
        System.out.println("in A parameterized constructor");
    }
}

class  B extends A{
    public B(){
        super();
        System.out.println("in B constructor");
    }

    public B(int n){
        this(); //call constructor of same class
        System.out.println("in B parameterized constructor");
    }
}

public class ThisAndSuperMethod {
    public static void main(String[] args) {
        // B obj = new B();
        B obj1 = new B(5);
    }
}

