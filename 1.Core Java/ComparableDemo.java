import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Student1 implements Comparable<Student1> {
    int age;
    String name;

    public Student1(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public String toString() {
        return "Student [age=" + age + ", name=" + name + "]";
    }

    public int compareTo(Student1 that) {
        if (this.age > that.age)
            return 1;
        else
            return -1;
    }

}

public class ComparableDemo {
    public static void main(String[] args) {
        List<Student1> studs = new ArrayList<>();
        studs.add(new Student1(21, "Navin"));
        studs.add(new Student1(12, "John"));
        studs.add(new Student1(18, "Parul"));
        studs.add(new Student1(20, "Kiran"));

        Collections.sort(studs);

        for (Student1 s : studs) {
            System.out.println(s);
        }
    }
}
