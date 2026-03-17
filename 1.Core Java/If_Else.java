public class If_Else {
    public static void main(String[] args) {
        int number = 10;

        //If statement
        if(number>0){
            System.out.println("The number is positive.");
        }

        //If-Else Statement
        if(number%2==0){
            System.out.println("The number is even.");
        } else {
            System.out.println("The number is odd.");
        }


        //Another example with multiple conditions
        int age=25;
        int weight=70;
        if(age>18 && weight>50){
            System.out.println("You are eligible to donate blood.");
        } else{
            System.out.println("You are not eligible to donate blood.");
        }
    }
}
