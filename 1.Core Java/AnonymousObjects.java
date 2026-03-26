class A {
    public A(){
        System.out.println("object created");
    }
    public void show(){
        System.out.println("in A show");
    }

    
}

public class AnonymousObjects {
    public static void main(String[] args) {
        new A();  //Anonymous object
        new A().show();  //Anonymous object
    }
}
