class Student {
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
        Student obj = new Student();
        obj.show();

        // Student.Address obj2 = obj.new Address();
        // obj2.config();

        Student.Address obj1 = new Student.Address();
        obj1.config();
    }
}
