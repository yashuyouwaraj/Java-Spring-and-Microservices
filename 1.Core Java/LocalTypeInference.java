class Alien{
    
}
public class LocalTypeInference {

    // var num=10;  //Local type inference is not allowed here
    public static void main(String[] args) {
        int a =10;
        var b = 20;  //Local type inference

        int nums[] = new int[10];
        var nums2 = new int[10];  //Local type inference

        var obj = new Alien();
    }
}
