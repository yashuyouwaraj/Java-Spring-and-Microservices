class Student{
    int rollno;
    String name;
    int marks;
}


public class Array_of_Objects {
    public static void main(String[] args) {
        Student s1 = new Student();
        s1.rollno=1;
        s1.name="John";
        s1.marks=85;

        Student s2 = new Student();
        s2.rollno=2;
        s2.name="Alice";
        s2.marks=90;

        Student s3 = new Student();
        s3.rollno=3;
        s3.name="Bob";
        s3.marks=80;

        // System.out.println("Student 1: Roll No: "+s1.rollno+", Name: "+s1.name+", Marks: "+s1.marks);

        Student students[] = new Student[3];
        students[0]=s1;
        students[1]=s2;
        students[2]=s3;

        // for(int i=0;i<students.length;i++){
        //     System.out.println("Student "+(i+1)+": Roll No: "+students[i].rollno+", Name: "+students[i].name+", Marks: "+students[i].marks);
        // }

        for(Student stud:students){
            System.out.println("Student: Roll No: "+stud.rollno+", Name: "+stud.name+", Marks: "+stud.marks);
        }
    }
}
