class Student2 {
    int age;

    public void show() {
        System.out.println("in show");
    }

    // class Address {
    // public void config(){
    // System.out.println("in config");
    // }
    // }

    static class Address {
        public void config() {
            System.out.println("in config");
        }
    }

}

public class Innerclass {
    public static void main(String[] args) {
        Student2 obj = new Student2();
        obj.show();

        // Student.Address obj2 = obj.new Address();
        // obj2.config();

        Student2.Address obj1 = new Student2.Address();
        obj1.config();
    }
}
