@FunctionalInterface
interface A16 {
    void show();
}

public class FunctionalInterfaceNew {
    public static void main(String[] args) {
        A16 obj = new A16() {
            @Override
            public void show() {
                System.out.println("in show");
            };
        };
        obj.show();
    };
};
