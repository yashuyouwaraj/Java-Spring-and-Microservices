class Calculator {
    public int add(int a, int b){
        return a+b;
    }
    public int add(int a, int b, int c){
        return a+b+c;
    }
    public double add(double a, int b){
        return a+b;
    }
    
}

public class MethodOverloading {
    public static void main(String[] args) {
        Calculator objCalculator = new Calculator();
        int sum1= objCalculator.add(10, 20);
        int sum2= objCalculator.add(10, 20, 30);
        double sum3= objCalculator.add(10.5, 20);
        System.out.println("The sum of 10 and 20 is: "+sum1);
        System.out.println("The sum of 10, 20 and 30 is: "+sum2);
        System.out.println("The sum of 10.5 and 20 is: "+sum3);
    }
}
