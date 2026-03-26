class A{

}

class B extends A{

}

// class C extends A,B{
//  Multiple inheritance does not supported by Java
// Due to Ambiguity issue
// }

class C extends B{

}

public class MultipleInheritance {
    public static void main(String[] args) {
        
    }
}
// Multiple Inheritance: A class can inherit from more than one class, thus forming a multiple inheritance. In this example, class C cannot inherit from both class A and class B because Java does not support multiple inheritance due to the ambiguity issue that arises when two parent classes have methods with the same signature. However, class C can inherit from class B, which in turn inherits from class A, thus forming a multilevel inheritance.