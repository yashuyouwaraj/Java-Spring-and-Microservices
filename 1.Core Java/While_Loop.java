public class While_Loop {
    public static void main(String[] args) {
        int i = 1;
        while (i<=5){
            System.out.println("Hi "+i);
            i++;
        }

        int j=1;
        while (j<=5){
            System.out.println("Hello "+j);
            int z=1;
            while (z<=3) {
                System.out.println("Welcome " + z);
                z++;
            }
            j++;
        }
    }
}
