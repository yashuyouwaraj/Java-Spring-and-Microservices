class A{
    public void show(){
        System.out.println("in A show");
    }
    public void display(){
        System.out.println("in A display");
    }
}

class B extends A{
    public void show(){
        System.out.println("in B show");
    }
    public void display(int n){
        System.out.println("in B display");
    }
}

public class MethodOverriding {
    public static void main(String[] args) {
        B objB = new B();

        objB.show(); // Calls B's show method
        objB.display(5); // Calls B's display method
    }
}

// Method Overriding: When a subclass provides a specific implementation of a method that is already defined in its superclass, it is called method overriding. In this example, the show() method in class B overrides the show() method in class A. When we call objB.show(), it executes the show() method defined in class B, not the one in class A. However, the display() method in class B does not override the display() method in class A because it has a different parameter list (method signature). Therefore, when we call objB.display(5), it executes the display(int n) method defined in class B, not the display() method in class A.