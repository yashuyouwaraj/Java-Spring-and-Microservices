@FunctionalInterface
interface A17{
    void show();
}

@FunctionalInterface
interface A18{
    void show1(int i);
}

@FunctionalInterface
interface A19{
    void show2(int i, int j);
}

@FunctionalInterface
interface A20{
    int add(int i, int j);
}


public class LambdaExpression {
    public static void main(String[] args) {
        A17 obj = () -> System.out.println("in show");
        obj.show();

        A18 obj1 = (int i) -> System.out.println("in show1 : " + i);
        obj1.show1(5);

        A19 obj2 = (i, j) -> System.out.println("in show2 : " + i + " & " + j);
        obj2.show2(5, 10);

        A20 obj3 = (i,j)-> i+j;
        System.out.println("in add: " + obj3.add(5,10));
    }
}
