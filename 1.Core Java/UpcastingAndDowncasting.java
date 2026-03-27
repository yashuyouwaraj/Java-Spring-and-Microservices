class A6
{
	public void show1()
	{
		System.out.println("in A show");
	}
}
class B6 extends A6
{
	public void show2()
	{
		System.out.println("in show B");
	}
}

public class UpcastingAndDowncasting {
    public static void main(String[] args) {
        A6 obj = new B6(); // Upcasting: B6 is being upcasted to A6, this is implicit and does not require a cast
        obj.show1();

        A6 obj1 = new B6();
        B6 obj2 = (B6) obj1; // Downcasting: A6 is being downcasted to B6, this is explicit and requires a cast
        obj2.show2();

    }
}
